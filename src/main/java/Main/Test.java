package Main;

/**
 * Created by huuphuoc on 7/5/16.
 */

import Utils.MusicFrequency;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.io.*;
import javax.sound.sampled.*;
import java.util.*;


public class Test {

    public static void main(String[] args) {
//        Test test = new Test();
//        MusicFrequency musicFrequency = MusicFrequency.Do;
//        test.test(musicFrequency);
//        test.capture();
//        AudioTest audioTest = new AudioTest();
//        audioTest.main();
//        Process process = new Process();
//        try {
//            process.main();
//        } catch (Exception e) {
//            e.getStackTrace();
//        }

    }

    public void capture() {
        ByteArrayOutputStream byteArrayOutputStream;
        TargetDataLine targetDataLine;
        int count;
        boolean stopCapture = false;
        byte tempBuffer[] = new byte[8000];
        int countzero, countdownTimer;
        short convert[] = new short[tempBuffer.length];
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            stopCapture = false;
            countdownTimer = 0;
            while (!stopCapture) {
                AudioFormat audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                count = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                byteArrayOutputStream.write(tempBuffer, 0, count);
                try {
                    countzero = 0;
                    for (int i = 0; i < tempBuffer.length; i++) {
                        convert[i] = tempBuffer[i];
                        if (convert[i] == 0) {
                            countzero++;
                        }
                    }

                    countdownTimer++;
                    System.out.println(countzero + " " + countdownTimer);

                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
                Thread.sleep(0);
                targetDataLine.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void test(MusicFrequency musicFrequency) {
        switch (musicFrequency) {
            case Do:
                System.out.println("Do");
                break;
            case La:
                System.out.printf("La");
                break;
            default:
                System.out.println("Not found");
                break;


        }

        System.out.println();


    }
}

class AudioTest {

    public void main() {

        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        TargetDataLine microphone;
        AudioInputStream audioInputStream;
        SourceDataLine sourceDataLine;
        try {
            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            int bytesRead = 0;

            try {
                while (bytesRead < 100000) { // Just so I can test if recording
                    // my mic works...
                    numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                    bytesRead = bytesRead + numBytesRead;
                    System.out.println(bytesRead);
                    out.write(data, 0, numBytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte audioData[] = out.toByteArray();
            // Get an input stream on the byte array
            // containing the data
            InputStream byteArrayInputStream = new ByteArrayInputStream(
                    audioData);
            audioInputStream = new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            int count = 0;
            byte tempBuffer[] = new byte[10000];
            try {
                while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (count > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        sourceDataLine.write(tempBuffer, 0, count);
                    }// end if
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Block and wait for internal buffer of the
            // data line to empty.
            sourceDataLine.drain();
            sourceDataLine.close();
            microphone.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}



class DFT {

    static void dft(double[] inR, double[] outR, double[] outI) {
        for (int k = 0; k < inR.length; k++) {
            for (int t = 0; t < inR.length; t++) {
                outR[k] += inR[t] * Math.cos(2 * Math.PI * t * k / inR.length);
                outI[k] -= inR[t] * Math.sin(2 * Math.PI * t * k / inR.length);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        float sampleRate = 44100;
        double f1 = 261.626;
        double f2 = 329.628;
        double a = .5;
        double twoPiF1 = 2 * Math.PI * f1;
        double twoPiF2 = 2 * Math.PI * f2;

        double[] bufferR = new double[2048];
        for (int sample = 0; sample < bufferR.length; sample++) {
            double time = sample / sampleRate;
            bufferR[sample] = a * (Math.sin(twoPiF1 * time) + Math.sin(twoPiF2 * time)) / 2;
        }

        double[] outR = new double[bufferR.length];
        double[] outI = new double[bufferR.length];

        dft(bufferR, outR, outI);
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex resultC[] = fft.transform(bufferR, TransformType.FORWARD);

        double results[] = new double[outR.length];
        for (int i = 0; i < outR.length; i++) {
            results[i] = Math.sqrt(outR[i] * outR[i] + outI[i] * outI[i]);
        }
        for (int i = 0; i < resultC.length; i++) {
            double real = resultC[i].getReal();
            double imaginary = resultC[i].getImaginary();
            results[i] = Math.sqrt(real * real + imaginary * imaginary);
        }

//        List<Float> found = process(results, sampleRate, bufferR.length, 4);
//        for (float freq : found) {
//            System.out.println("Found: " + freq);
//        }
    }


}