import java.io.*;
import java.net.*;
import java.util.*;

public class SDESServer {

    static int[] P10 = {3,5,2,7,4,10,1,9,8,6};
    static int[] P8 = {6,3,7,4,8,5,10,9};
    static int[] IP = {2,6,3,1,4,8,5,7};
    static int[] IP1 = {4,1,3,5,7,2,8,6};
    static int[] EP = {4,1,2,3,2,3,4,1};
    static int[] P4 = {2,4,3,1};

    static int[][] S0 = {
        {1,0,3,2},
        {3,2,1,0},
        {0,2,1,3},
        {3,1,3,2}
    };

    static int[][] S1 = {
        {0,1,2,3},
        {2,0,1,3},
        {3,0,1,0},
        {2,1,0,3}
    };

    static int[] permute(int[] bits, int[] p) {
        int[] out = new int[p.length];
        for(int i=0;i<p.length;i++)
            out[i]=bits[p[i]-1];
        return out;
    }

    static int[] leftShift(int[] bits,int n){
        int[] out=new int[bits.length];
        for(int i=0;i<bits.length;i++)
            out[i]=bits[(i+n)%bits.length];
        return out;
    }

    static int[] xor(int[] a,int[] b){
        int[] out=new int[a.length];
        for(int i=0;i<a.length;i++)
            out[i]=a[i]^b[i];
        return out;
    }

    static int[] sbox(int[] bits,int[][] box){
        int row=bits[0]*2+bits[3];
        int col=bits[1]*2+bits[2];
        int val=box[row][col];
        return new int[]{(val>>1)&1,val&1};
    }

    static int[] fk(int[] bits,int[] key){

        int[] L=Arrays.copyOfRange(bits,0,4);
        int[] R=Arrays.copyOfRange(bits,4,8);

        int[] temp=permute(R,EP);
        temp=xor(temp,key);

        int[] left=sbox(Arrays.copyOfRange(temp,0,4),S0);
        int[] right=sbox(Arrays.copyOfRange(temp,4,8),S1);

        int[] combine={left[0],left[1],right[0],right[1]};
        combine=permute(combine,P4);

        L=xor(L,combine);

        return new int[]{
                L[0],L[1],L[2],L[3],
                R[0],R[1],R[2],R[3]
        };
    }

    static int[] swap(int[] bits){
        return new int[]{
                bits[4],bits[5],bits[6],bits[7],
                bits[0],bits[1],bits[2],bits[3]
        };
    }

    static String encrypt(String keyStr,String ptStr){

        int[] key=new int[10];
        int[] pt=new int[8];

        for(int i=0;i<10;i++)
            key[i]=keyStr.charAt(i)-'0';

        for(int i=0;i<8;i++)
            pt[i]=ptStr.charAt(i)-'0';

        key=permute(key,P10);

        int[] left=Arrays.copyOfRange(key,0,5);
        int[] right=Arrays.copyOfRange(key,5,10);

        left=leftShift(left,1);
        right=leftShift(right,1);

        int[] temp={
                left[0],left[1],left[2],left[3],left[4],
                right[0],right[1],right[2],right[3],right[4]
        };

        int[] K1=permute(temp,P8);

        left=leftShift(left,2);
        right=leftShift(right,2);

        temp=new int[]{
                left[0],left[1],left[2],left[3],left[4],
                right[0],right[1],right[2],right[3],right[4]
        };

        int[] K2=permute(temp,P8);

        pt=permute(pt,IP);
        pt=fk(pt,K1);
        pt=swap(pt);
        pt=fk(pt,K2);
        pt=permute(pt,IP1);

        StringBuilder sb=new StringBuilder();

        for(int bit:pt)
            sb.append(bit);

        return sb.toString();
    }

    public static void main(String[] args)throws Exception{

        ServerSocket server=new ServerSocket(5000);

        System.out.println("Server Started...");
        System.out.println("Waiting for Client...");

        Socket socket=server.accept();

        System.out.println("Client Connected.");

        DataInputStream in=new DataInputStream(socket.getInputStream());
        DataOutputStream out=new DataOutputStream(socket.getOutputStream());

        String key=in.readUTF();
        String pt=in.readUTF();

        System.out.println("Key: "+key);
        System.out.println("Plaintext: "+pt);

        String cipher=encrypt(key,pt);

        out.writeUTF(cipher);

        System.out.println("Ciphertext Sent: "+cipher);

        socket.close();
        server.close();
    }
}
