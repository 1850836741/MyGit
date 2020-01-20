package com.example.oauth2_server.tool;

public class ByteTool {
    public static int BytesToInt(byte[] bytes, int start, int length){
        int n;                                  //中间传值变量
        int sum = 0;
        int end=start+length;
        for (int i = start;i < end; i++){
            n=((int)bytes[i]) & 0xff;
            n<<=(--length)*8;
            sum=n+sum;
        }
        return sum;
    }

    public static byte[] IntToBytes(int value, int length){
        byte[] bytes=new byte[length];
        for (int i = 0;i < length;i++){
            bytes[length-i-1] =(byte) ((value>>i*8) & 0xff);
        }
        return bytes;
    }

    public static String BytesToString(byte[] bytes,int start,int length){
        return new String(bytes,start,length);
    }

    public static byte[] StringToBytes(String string){
        return string.getBytes();
    }

    public static byte[] BytesReplace(byte[] originalBytes, byte[] replaceBytes, int offset, int length){
        byte[] newBytes = new byte[originalBytes.length-length+replaceBytes.length];
        //arraycopy参数为:被复制数组及其起始位置，复制出来的数组及其起始位置，复制长度
        System.arraycopy(originalBytes,0,newBytes,0,offset);
        System.arraycopy(replaceBytes,0,newBytes,offset,replaceBytes.length);
        System.arraycopy(originalBytes,offset+length,newBytes,offset+replaceBytes.length,originalBytes.length-offset-length);
        return newBytes;
    }
}
