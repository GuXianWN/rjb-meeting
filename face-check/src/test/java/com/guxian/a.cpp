// PSNR.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"

#include <iostream>                   // Console I/O
#include <sstream>                    // String to number conversion

#include <opencv2/core/core.hpp>      // Basic OpenCV structures
#include <opencv2/imgproc/imgproc.hpp>// Image processing methods for the CPU
#include <opencv2/highgui/highgui.hpp>// Read images
#include <opencv2/gpu/gpu.hpp>        // GPU structures and methods

using namespace std;
using namespace cv;

double getPSNR(const Mat& I1, const Mat& I2);      // CPU versions
Scalar getMSSIM( const Mat& I1, const Mat& I2);

double getPSNR_GPU(const Mat& I1, const Mat& I2);  // Basic GPU versions
Scalar getMSSIM_GPU( const Mat& I1, const Mat& I2);

struct BufferPSNR                                     // Optimized GPU versions
{   // Data allocations are very expensive on GPU. Use a buffer to solve: allocate once reuse later.
	gpu::GpuMat gI1, gI2, gs, t1,t2;

	gpu::GpuMat buf;
};
double getPSNR_GPU_optimized(const Mat& I1, const Mat& I2, BufferPSNR& b);

struct BufferMSSIM                                     // Optimized GPU versions
{   // Data allocations are very expensive on GPU. Use a buffer to solve: allocate once reuse later.
	gpu::GpuMat gI1, gI2, gs, t1,t2;

	gpu::GpuMat I1_2, I2_2, I1_I2;
	vector<gpu::GpuMat> vI1, vI2;

	gpu::GpuMat mu1, mu2;
	gpu::GpuMat mu1_2, mu2_2, mu1_mu2;

	gpu::GpuMat sigma1_2, sigma2_2, sigma12;
	gpu::GpuMat t3;

	gpu::GpuMat ssim_map;

	gpu::GpuMat buf;
};
Scalar getMSSIM_GPU_optimized( const Mat& i1, const Mat& i2, BufferMSSIM& b);

void help()
{
	cout
		<< "\n--------------------------------------------------------------------------" << endl
		<< "This program shows how to port your CPU code to GPU or write that from scratch." << endl
		<< "You can see the performance improvement for the similarity check methods (PSNR and SSIM)."  << endl
		<< "Usage:"                                                               << endl
		<< "./gpu-basics-similarity referenceImage comparedImage numberOfTimesToRunTest(like 10)." << endl
		<< "--------------------------------------------------------------------------"   << endl
		<< endl;
}

int main(int argc, char *argv[])
{
	help();
	Mat I1 = imread("swan1.jpg",1);           // Read the two images
	Mat I2 = imread("swan2.jpg",1);

	if (!I1.data || !I2.data)           // Check for success
	{
		cout << "Couldn't read the image";
		return 0;
	}

	BufferPSNR bufferPSNR;
	BufferMSSIM bufferMSSIM;

	int TIMES;
	stringstream sstr("500");
	sstr >> TIMES;
	double time, result;

	//------------------------------- PSNR CPU ----------------------------------------------------

	for (int i = 0; i < TIMES; ++i)
		result = getPSNR(I1,I2);



	getchar();
}




// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------
double getPSNR_GPU_optimized(const Mat& I1, const Mat& I2, BufferPSNR& b)
{
	b.gI1.upload(I1);
	b.gI2.upload(I2);

	b.gI1.convertTo(b.t1, CV_32F);
	b.gI2.convertTo(b.t2, CV_32F);

	gpu::absdiff(b.t1.reshape(1), b.t2.reshape(1), b.gs);
	gpu::multiply(b.gs, b.gs, b.gs);

	double sse = gpu::sum(b.gs, b.buf)[0];
	if( sse <= 1e-10) // for small values return zero
		return 0;
	else
	{
		double mse = sse /(double)(I1.channels() * I1.total());
		double psnr = 10.0*log10((255*255)/mse);
		return psnr;
	}
}

double getx_GPU(const Mat& I1, const Mat& I2)
{
	gpu::GpuMat gI1, gI2, gs, t1,t2;

	gI1.upload(I1);
	gI2.upload(I2);

	gI1.convertTo(t1, CV_32F);
	gI2.convertTo(t2, CV_32F);

	gpu::absdiff(t1.reshape(1), t2.reshape(1), gs);
	gpu::multiply(gs, gs, gs);

	Scalar s = gpu::sum(gs);
	double sse = s.val[0] + s.val[1] + s.val[2];

	if( sse <= 1e-10) // for small values return zero
		return 0;
	else
	{
		double  mse =sse /(double)(gI1.channels() * I1.total());
		double psnr = 10.0*log10((255*255)/mse);
		return psnr;
	}
}


Scalar getMSSIM_GPU( const Mat& i1, const Mat& i2)
{
	const float C1 = 6.5025f, C2 = 58.5225f;
	/***************************** INITS **********************************/
	gpu::GpuMat gI1, gI2, gs1, t1,t2;

	gI1.upload(i1);
	gI2.upload(i2);

	gI1.convertTo(t1, CV_MAKE_TYPE(CV_32F, gI1.channels()));
	gI2.convertTo(t2, CV_MAKE_TYPE(CV_32F, gI2.channels()));

	vector<gpu::GpuMat> vI1, vI2;
	gpu::split(t1, vI1);
	gpu::split(t2, vI2);
	Scalar mssim;

	for( int i = 0; i < gI1.channels(); ++i )
	{
		gpu::GpuMat I2_2, I1_2, I1_I2;

		gpu::multiply(vI2[i], vI2[i], I2_2);        // I2^2
		gpu::multiply(vI1[i], vI1[i], I1_2);        // I1^2
		gpu::multiply(vI1[i], vI2[i], I1_I2);       // I1 * I2

		/*************************** END INITS **********************************/
		gpu::GpuMat mu1, mu2;   // PRELIMINARY COMPUTING
		gpu::GaussianBlur(vI1[i], mu1, Size(11, 11), 1.5);
		gpu::GaussianBlur(vI2[i], mu2, Size(11, 11), 1.5);

		gpu::GpuMat mu1_2, mu2_2, mu1_mu2;
		gpu::multiply(mu1, mu1, mu1_2);
		gpu::multiply(mu2, mu2, mu2_2);
		gpu::multiply(mu1, mu2, mu1_mu2);

		gpu::GpuMat sigma1_2, sigma2_2, sigma12;

		gpu::GaussianBlur(I1_2, sigma1_2, Size(11, 11), 1.5);
		//sigma1_2 = sigma1_2 - mu1_2;
		gpu::subtract(sigma1_2,mu1_2,sigma1_2);

		gpu::GaussianBlur(I2_2, sigma2_2, Size(11, 11), 1.5);
		//sigma2_2 = sigma2_2 - mu2_2;

		gpu::GaussianBlur(I1_I2, sigma12, Size(11, 11), 1.5);
		(Mat)sigma12 =(Mat)sigma12 - (Mat)mu1_mu2;
		//sigma12 = sigma12 - mu1_mu2

		///////////////////////////////// FORMULA ////////////////////////////////
		gpu::GpuMat t1, t2, t3;

// 		t1 = 2 * mu1_mu2 + C1;
// 		t2 = 2 * sigma12 + C2;
// 		gpu::multiply(t1, t2, t3);     // t3 = ((2*mu1_mu2 + C1).*(2*sigma12 + C2))
//
// 		t1 = mu1_2 + mu2_2 + C1;
// 		t2 = sigma1_2 + sigma2_2 + C2;
// 		gpu::multiply(t1, t2, t1);     // t1 =((mu1_2 + mu2_2 + C1).*(sigma1_2 + sigma2_2 + C2))

		gpu::GpuMat ssim_map;
		gpu::divide(t3, t1, ssim_map);      // ssim_map =  t3./t1;

		Scalar s = gpu::sum(ssim_map);
		mssim.val[i] = s.val[0] / (ssim_map.rows * ssim_map.cols);

	}
	return mssim;
}

Scalar getMSSIM_GPU_optimized( const Mat& i1, const Mat& i2, BufferMSSIM& b)
{
	int cn = i1.channels();

	const float C1 = 6.5025f, C2 = 58.5225f;
	/***************************** INITS **********************************/

	b.gI1.upload(i1);
	b.gI2.upload(i2);

	gpu::Stream stream;

	stream.enqueueConvert(b.gI1, b.t1, CV_32F);
	stream.enqueueConvert(b.gI2, b.t2, CV_32F);

	gpu::split(b.t1, b.vI1, stream);
	gpu::split(b.t2, b.vI2, stream);
	Scalar mssim;

	for( int i = 0; i < b.gI1.channels(); ++i )
	{
		gpu::multiply(b.vI2[i], b.vI2[i], b.I2_2, stream);        // I2^2
		gpu::multiply(b.vI1[i], b.vI1[i], b.I1_2, stream);        // I1^2
		gpu::multiply(b.vI1[i], b.vI2[i], b.I1_I2, stream);       // I1 * I2

		//gpu::GaussianBlur(b.vI1[i], b.mu1, Size(11, 11), 1.5, 0, BORDER_DEFAULT, -1, stream);
		//gpu::GaussianBlur(b.vI2[i], b.mu2, Size(11, 11), 1.5, 0, BORDER_DEFAULT, -1, stream);

		gpu::multiply(b.mu1, b.mu1, b.mu1_2, stream);
		gpu::multiply(b.mu2, b.mu2, b.mu2_2, stream);
		gpu::multiply(b.mu1, b.mu2, b.mu1_mu2, stream);

		//gpu::GaussianBlur(b.I1_2, b.sigma1_2, Size(11, 11), 1.5, 0, BORDER_DEFAULT, -1, stream);
		//gpu::subtract(b.sigma1_2, b.mu1_2, b.sigma1_2, stream);
		//b.sigma1_2 -= b.mu1_2;  - This would result in an extra data transfer operation

		//gpu::GaussianBlur(b.I2_2, b.sigma2_2, Size(11, 11), 1.5, 0, BORDER_DEFAULT, -1, stream);
		//gpu::subtract(b.sigma2_2, b.mu2_2, b.sigma2_2, stream);
		//b.sigma2_2 -= b.mu2_2;

		//gpu::GaussianBlur(b.I1_I2, b.sigma12, Size(11, 11), 1.5, 0, BORDER_DEFAULT, -1, stream);
		//gpu::subtract(b.sigma12, b.mu1_mu2, b.sigma12, stream);
		//b.sigma12 -= b.mu1_mu2;

		//here too it would be an extra data transfer due to call of operator*(Scalar, Mat)
		gpu::multiply(b.mu1_mu2, 2, b.t1, stream); //b.t1 = 2 * b.mu1_mu2 + C1;
		//gpu::add(b.t1, C1, b.t1, stream);
		gpu::multiply(b.sigma12, 2, b.t2, stream); //b.t2 = 2 * b.sigma12 + C2;
		//gpu::add(b.t2, C2, b.t2, stream);

		gpu::multiply(b.t1, b.t2, b.t3, stream);     // t3 = ((2*mu1_mu2 + C1).*(2*sigma12 + C2))

		//gpu::add(b.mu1_2, b.mu2_2, b.t1, stream);
		//gpu::add(b.t1, C1, b.t1, stream);

		//gpu::add(b.sigma1_2, b.sigma2_2, b.t2, stream);
		//gpu::add(b.t2, C2, b.t2, stream);


		gpu::multiply(b.t1, b.t2, b.t1, stream);     // t1 =((mu1_2 + mu2_2 + C1).*(sigma1_2 + sigma2_2 + C2))
		gpu::divide(b.t3, b.t1, b.ssim_map, stream);      // ssim_map =  t3./t1;

		stream.waitForCompletion();

		Scalar s = gpu::sum(b.ssim_map, b.buf);
		mssim.val[i] = s.val[0] / (b.ssim_map.rows * b.ssim_map.cols);

	}
	return mssim;
}