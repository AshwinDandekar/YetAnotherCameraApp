package com.example.asd29.findthoseips;

import java.util.Objects;

/**
 * Created by asd29 on 1/29/2016.
 */
public class IPAddress extends Object {
    int byte1;
    int byte2;
    int byte3;
    int byte4;

    public IPAddress(int inputbyte1 ,int inputbyte2, int inputbyte3, int inputbyte4) {
        this.byte1 = inputbyte1;
        this.byte2 = inputbyte2;
        this.byte3 = inputbyte3;
        this.byte4 = inputbyte4;
    }

    public void setByte1(int byte1) {
        this.byte1 = byte1;
    }

    public void setByte2(int byte2) {
        this.byte2 = byte2;
    }

    public void setByte3(int byte3) {
        this.byte3 = byte3;
    }

    public void setByte4(int byte4) {
        this.byte4 = byte4;
    }

    public int getByte1() {
        return byte1;
    }

    public int getByte2() {
        return byte2;
    }

    public int getByte3() {
        return byte3;
    }

    public int getByte4() {
        return byte4;
    }
}

