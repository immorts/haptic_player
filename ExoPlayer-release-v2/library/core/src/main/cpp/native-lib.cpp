#include <jni.h>
#include <string>
#include <string.h>
#include<math.h>
#include<cmath>
#include<stdlib.h>
#include <bitset>
#include <iomanip>
using namespace std;
extern "C" JNIEXPORT jstring JNICALL
Java_com_google_android_exoplayer2_audio_DefaultAudioSink_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}



jdouble b[7] = { 0.000000027665352424989662058886922602931 ,0.000000165992114549937978970766436041806 ,0.000000414980286374844960661805890952958 ,0.000000553307048499793175003289447816401 ,0.000000414980286374844907722246687559187 ,0.000000165992114549937978970766436041806 ,0.000000027665352424989662058886922602931 };
jdouble a[7] = { 1 , -5.559659044717919940126193978358060121536 , 12.894199797554115605180413695052266120911 ,-15.967129150803591741691889183130115270615 , 11.133950695208785219847413827665150165558 , -4.144952638888367957292757637333124876022 ,  0.643592112229536827960885148058878257871 };

//
//extern "C" JNIEXPORT jdoubleArray JNICALL
//Java_com_google_android_exoplayer2_audio_DefaultAudioSink_filterFromJNI( JNIEnv* env,jobject jobj,jdoubleArray x1,  jint s_n) {
//    int n = 7;
//    jdoubleArray y1;
//    y1= (env)->NewDoubleArray(s_n);
//    jdouble *y = (double*)malloc(sizeof(double)*s_n);
//    jdouble *x = env->GetDoubleArrayElements(x1,NULL);
//
//
//
//    for (int i = 0; i < s_n; i++) {
//        if (i < n) {
//            for (int k = 0; k <= i; k++) {
////                y[i] += b[k] * x[i - k];
//                y[i] += b[k] * x[i - k];
//            }
//            for (int k = 1; k <= i; k++) {
//                y[i] -= a[k] * y[i - k];
//            }
//        }
//        else {
//            for (int m = 0; m < n; m++) {
//                y[i] += b[m] * x[i - m];
//            }
//            for (int m = 1; m < n; m++) {
//                y[i] -= a[m] * y[i - m];
//            }
//        }
//    }
//
//    env->SetDoubleArrayRegion(y1,0,s_n,y);
//
//    return y1;
//}


extern "C" JNIEXPORT jdoubleArray JNICALL
Java_com_google_android_exoplayer2_source_ProgressiveMediaPeriod_filterFromJNI( JNIEnv* env,jobject jobj,jdoubleArray x1,  jint s_n) {
    int n=7;
    jdoubleArray y1;
    y1= (env)->NewDoubleArray(s_n);
    jdouble *y=(double*)malloc(sizeof(double)*s_n);
    jdouble *x=env->GetDoubleArrayElements(x1,NULL);



    for (int i = 0; i < s_n/2; i++) {
        if (i < n) {
            for (int k = 0; k <= i; k++) {
//                y[i] += b[k] * x[i - k];
                y[2*i] += b[k] * x[(i - k)*2];
            }
            for (int k = 1; k <= i; k++) {
                y[2*i] -= a[k] * y[(i - k) * 2];
            }
        }
        else {
            for (int m = 0; m < n; m++) {
                y[2*i] += b[m] * x[(i - m)*2];
            }
            for (int m = 1; m < n; m++) {
                y[2*i] -= a[m] * y[(i - m)*2];
            }
        }
    }

    for (int i = 0; i < s_n/2; i++) {
        if (i < n) {
            for (int k = 0; k <= i; k++) {
                //                y[i] += b[k] * x[i - k];
                y[2*i+1] += b[k] * x[(i - k) * 2+1];
            }
            for (int k = 1; k <= i; k++) {
                y[2*i+1] -= a[k] * y[(i - k) * 2 + 1];
            }
        }
        else {
            for (int m = 0; m < n; m++) {
                y[2*i+1] += b[m] * x[(i - m) * 2+1];
            }
            for (int m = 1; m < n; m++) {
                y[2*i+1] -= a[m] * y[(i - m) * 2+1];
            }
        }
    }

    env->SetDoubleArrayRegion(y1,0,s_n,y);

    return y1;
}





