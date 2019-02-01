package com.example.demo.util;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.zip.*;
import java.util.*;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.awt.geom.AffineTransform ;

public class M
{
  private static String[] MONTH =
  {
   "Error",
   ",dik8,", "d6,4kryoTN", ",uok8,",
   "g,Kkpo", "rAK4k8,", ",b56okpo",
   "didEk8,", "lb'sk8,", "dyopkpo",
   "96]k8,", "rAL0bdkpo", "Tyo;k8,"
  };
  private static String[] N =
  { "so7j'", "lv'", "lk,", "luj","shk","sd","g0Hf","cxf","gdhk" };
  private static String[] M =
  { "clo", "s,njo", "ryo" };
  private static String BAHT  = "[km";
  private static String STANG = "l9k'8N";
  private static String ONE = "gvHf";
  private static String TWO = "puj";
  private static String TEN = "lb[";
  private static String HUNDRED = "ihvp";
  private static String MILLION = "]hko";

  private static boolean ASCII_SI = false; // CHANGE HERE !
  static final long serialVersionUID = 255102220001L ;

  /* ---------- BASIC ARITHMETICS ---------- */
  /** 
   @s1 is String number one
   @s2 is String number two
   @p3 is number of point 
   @return sum of  s1 and s2 with p3 point 
  */
  public static String addnum(String s1, String s2, int p3)
  {
    long n3 = addn( ctol(undot(s1)), ctol(undot(s2)),
                    nfdigit(s1), nfdigit(s2), p3);
    return endot(ltoc(n3),p3);
  }
  /**
   @return sum of  s1 and s2
  */
  public static String addnum(String s1, String s2)
  {
    int pmax = nfdigit(s1);
    int p = nfdigit(s2);
    if (pmax < p) pmax = p;
    return addnum(s1,s2,pmax);
  }
  /** 
    addn - add two long numbers 
    @return sum of two long numbers with p3 point
  */
  private static long addn(long n1, long n2,
                            int p1,  int p2, int p3)
  {
    int p = Math.abs(p1-p2);
    if ( p1 >= p2)
      while ( p-- > 0 ) n2 = n2*10;
    else
      while ( p-- > 0 ) n1 = n1*10;
    int pmax = p1 >= p2 ? p1 : p2;
    p = Math.abs(p3-pmax);
    long n3 = n1 + n2; // addnum
    boolean neg3 = n3 < 0 ? true : false;
    if (neg3) n3 = -n3;
    if (p3 >= pmax)
      while (p-- > 0) n3 = n3*10;
    else // p3 < pmax
      {
        while ( --p > 0 ) n3 = n3/10;
        n3 = n3+5; n3 = n3/10; // rounding off
      }
    if (neg3) n3 = -n3;
    return n3;
  }
  /**
     divide - performs String division 
  */
  public static String divide(String s1, String s2, int p3)
  {
    int p1 = nfdigit(s1);
    int p2 = nfdigit(s2);
    double n1 = ctol(undot(s1));
    double n2 = ctol(undot(s2));
    int neg1 = n1 < 0 ? -1 : 1;
    int neg2 = n2 < 0 ? -1 : 1;
    int neg3 = neg1*neg2;
    if ( neg1 < 0 ) n1 = -n1;
    if ( neg2 < 0 ) n2 = -n2;
    int p = Math.abs(p1-p2);
    if ( p1 > p2 ) while ( p-- > 0 ) n2 = n2*10;
    else while ( p-- > 0 ) n1 = n1*10;
    if ( n2 == 0 ) return new String(); // divide by zero
    long q = (long)(n1/n2);
    long r = (long)(n1 - n2*q);
    for (int i = 0; i < p3; i++) {
        q = q*10;
        r = r*10;
    }
    r = r*10; // here! already left shifted p3+1 digits
    r = (long)(r/n2); r = r+5; r = r/10;
    q = q + r;
    if (neg3 == -1) q = -q;
    return endot(ltoc(q),p3);
  }
  /* multiply - performs String multiplication */
  public static String multiply(String s1,String s2,int p3)
  {
    int p1 = nfdigit(s1);
    int p2 = nfdigit(s2);
    double n1 = ctol(undot(s1));
    double n2 = ctol(undot(s2));
    int neg3 = 1;
    if ( n1 < 0 ) neg3 = -neg3;
    if ( n2 < 0 ) neg3 = -neg3;
    int p = Math.abs(p1-p2);
    double n3 = Math.abs(n1)*Math.abs(n2);
    /* rounding-off */
    int pmax = p1+p2;
    p = Math.abs(pmax-p3);
    if (p3 >= pmax)
      while (p-- > 0) n3 = n3*10;
    else // p3 < pmax
      {
        while ( --p > 0 ) n3 = n3/10;
        n3 = n3+5; n3 = n3/10; // rounding off
      }
    if ( neg3 < 0 ) n3 = -n3;
    return endot(ltoc((long)n3),p3);
  }
  /* subnum(1) */
  public static String subnum(String s1,String s2,int p3)
  {
    long n3 = addn( ctol(undot(s1)), -ctol(undot(s2)),
                    nfdigit(s1), nfdigit(s2), p3);
    return endot(ltoc(n3),p3);
  }
  /* subnum(2) */
  public static String subnum(String s1, String s2)
  {
    int pmax = nfdigit(s1);
    int p = nfdigit(s2);
    if (pmax < p) pmax = p;
    return subnum(s1,s2,pmax);
  }

  /* DATA CONVERSIONS */
  /* ktos(1): KU byte to SI byte */
  public static byte ktos(byte k)
  {
    if ( inrange(144,k,153) ) return (byte)(k+96);
    if ( inrange(161,k,162) ) return k;
    if ( inrange(164,k,195) ) return (byte)(k+2);
    if ( inrange(196,k,203) ) return (byte)(k+3);
    if ( inrange(206,k,207) ) return (byte)(k+4);
    if ( inrange(208,k,212) ) return (byte)(k+16);
    if ( inrange(215,k,216) ) return (byte)(k+1);
    if ( inrange(217,k,220) ) return (byte)(k-5);
    if ( inrange(223,k,228) ) return (byte)(k+8);

    if ( k == (byte)154 )     return (byte)163;
    if ( k == (byte)155 )     return (byte)165;
    if ( k == (byte)156 )     return (byte)223;
    if ( k == (byte)157 )     return (byte)229;
    if ( k == (byte)158 )     return (byte)238;
    if ( k == (byte)159 )     return (byte)239;
    if ( k == (byte)163 )     return (byte)164;
    if ( k == (byte)204 )     return (byte)208;
    if ( k == (byte)205 )     return (byte)198;
    if ( k == (byte)213 )     return (byte)230;
    if ( k == (byte)214 )     return (byte)207;
    if ( k == (byte)221 )     return (byte)209;
    if ( k == (byte)222 )     return (byte)237;
    if ( k == (byte)229 )     return (byte)218;

    return k;
  }
  /* ktos(2): KU byte[] to SI byte[] */
  public static byte[] ktos(byte[]k)
  {
    byte[]s = new byte[k.length];
    for (int i = 0; i < k.length; i++) s[i] = ktos(k[i]);
    return s;
  }
  /* ktou(): KU byte[] to UNICODE String */
  public static String ktou(byte[] k)
  {
    char[] c = new char[k.length];
    for (int i = 0; i < k.length; i++) c[i] = stou(ktos(k[i]));
   return new String(c);
  }

  /* ktou(): KU byte to UNICODE char */
  public static char ktou(byte k)
  {
    return stou(ktos(k));
  }
  /* stok(1): SI byte to KU byte */
  public static byte stok(byte s)
  {
    if ( inrange(161,s,162) ) return s;
    if ( inrange(166,s,197) ) return (byte)(s-2);
    if ( inrange(199,s,206) ) return (byte)(s-3);
    if ( inrange(210,s,211) ) return (byte)(s-4);
    if ( inrange(212,s,215) ) return (byte)(s+5);
    if ( inrange(216,s,217) ) return (byte)(s-1);
    if ( inrange(224,s,228) ) return (byte)(s-16);
    if ( inrange(231,s,236) ) return (byte)(s-8);
    if ( inrange(240,s,249) ) return (byte)(s-96);

    if ( s == (byte)163 )     return (byte)154;
    if ( s == (byte)164 )     return (byte)163;
    if ( s == (byte)165 )     return (byte)155;
    if ( s == (byte)198 )     return (byte)205;
    if ( s == (byte)207 )     return (byte)214;
    if ( s == (byte)208 )     return (byte)204;
    if ( s == (byte)209 )     return (byte)221;
    if ( s == (byte)218 )     return (byte)229;
    if ( s == (byte)223 )     return (byte)156;     // baht sign
    if ( s == (byte)229 )     return (byte)157;     // long sara-r
    if ( s == (byte)230 )     return (byte)213;
    if ( s == (byte)237 )     return (byte)222;
    if ( s == (byte)238 )     return (byte)158;     // nuad tumlueng
    if ( s == (byte)239)      return (byte)159;     // cock's eye
    return s;
  }
  /* stok(2): SI byte[] to KU byte[] */
  public static byte[] stok(byte[]s)
  {
    byte[]k = new byte[s.length];
    for (int i = 0; i < s.length; i++) k[i] = stok(s[i]);
    return k;
  }

  /* stou(1): SI byte to UNICODE char */
   public static char stou(byte s)
   {
     char c = (char)(s & 0xff);
     return toucode(c);
  }
  /* stou(2): SI char to UNICODE char */
  public static char stou(char c)
  {
    return toucode(c);
  }
  /* stou(3): SI byte[] to UNICODE String */
  public static String stou(byte[]s)
  {
    char[]c = new char[s.length];
    for (int i = 0; i < s.length; i++) c[i] = stou(s[i]);
    return new String(c);
  }
  /* stou(4): SI String to UNICODE String */
  public static String stou(String s)
  {
    StringBuffer b = new StringBuffer(s);
    int l = b.length();
    for (int i = 0; i < l; i++) {
        char c = stou( b.charAt(i) );
        b.setCharAt(i,c);
    }
    return new String(b);
  }

  /* utok():UNICODE String to KU byte[] */
  public static byte[] utok(String u)
  {
    int l = u.length();
    if ( l == 0 ) return null;
    byte[] b = new byte[l];
    for (int i = 0; i < l; i++)
       b[i] = stok( (byte)utos(u.charAt(i)) );
    return b;
  }

  /* utos(1): UNICODE String to SI byte[] */
  /* Thai Industrial Standards Institute */
  public static byte[] utos(String s)
  {
    int l;
    if ( (l = s.length()) == 0 ) return null;
    byte[] b = new byte[l];
    for (int i = 0; i < l; i++)
        b[i] = (byte)utos( s.charAt(i) );
    return b;
  }
  /* utos(2):UNICODE char to SI char */
  public static char utos(char u)
  {
    return toscode(u);
  }
        // --------------------------------------------
        // convert KU byte array to unicode string
        // common use with swt and specific to Thailife
        // --------------------------------------------
        public static String btou(byte[] bs, int pos, int len)
        {
                return Ms.btou(bs, pos, len);
                /*
                int slen = len;
                int n = pos + len - 1;
                for (; n >= pos; n--, len--)
                        if (bs[n] != (byte)32)
                                break;
                if (n < pos)
                {
                        char[] c = new char[slen];
                        for (int i = 0; i < slen; i++)
                                c[i] = (char)32;
                        return new String(c);
                }
                char[] c = new char[len];
                int p = pos;
                for (int i = 0; i < len; i++, p++)
                        c[i] = stou(ktos(bs[p]));
                return new String(c);
                */
        }

  /* ---------- MISC ---------- */
  public static String ckdigit(String s)
  {
    int sum = 103;
    int w = 1;
    int l = s.length();
    for (int i = 0; i < l; i++, w++) {
        char c = s.charAt(i);
        sum = sum + (c-'0')*w;
    }
    sum = sum % 100;
    char c1 = (char)('0' + sum / 10);
    char c2 = (char)('0' + sum % 10);
    return s + c1 + c2;
  }
  /* clears - creates and initializes String */
  public static String clears(char c, int slen)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < slen; i++) b.append(c);
    return new String(b);
  }

  /* cmps - compare numeric String objects */
  public static int cmps(String s1, String s2)
  {
    int p1 = nfdigit(s1);
    int p2 = nfdigit(s2);
    StringBuffer t1 = new StringBuffer( undot(s1) );
    StringBuffer t2 = new StringBuffer( undot(s2) );
                if (t1.charAt(0) == '-' && itis(t1.substring(1),'0'))
        t1.deleteCharAt(0);
    if (t2.charAt(0) == '-' && itis(t2.substring(1),'0'))
        t2.deleteCharAt(0);

    int n1 = t1.charAt(0) == '-' ? 1 : 0;
    int n2 = t2.charAt(0) == '-' ? 1 : 0;
    if ( (n1 == 1 && n2 == 0) || (n1 == 0 && n2 == 1) )
      return (n2-n1);
    if (n1 == 1) t1.setCharAt(0,'0');
    if (n2 == 1) t2.setCharAt(0,'0');
    int p = Math.abs(p1-p2);
    if (p1 >= p2)
      while (p-- > 0) t2.append('0');
    else
      while (p-- > 0) t1.append('0');
    int l = t1.length();
    if ( l < t2.length() ) l = t2.length();
    while ( t1.length() < l ) t1.insert(0,'0');
    while ( t2.length() < l ) t2.insert(0,'0');
    int i = 0;
    for(; i < l && t1.charAt(i) == t2.charAt(i); i++);
    if (i == l) return 0;
    if (n1 == 1)
      n1 = -t1.charAt(i);
    else
      n1 =  t1.charAt(i);
    if (n2 == 1)
      n2 = -t2.charAt(i);
    else
      n2 =  t2.charAt(i);
    return ( n1 < n2 ? -1 : 1 );
  }
  /* ctoi(1) converts String to int */
  public static int ctoi(String s)
  {
    return (int)ctol(s);
  }
  /* ctoi(2) converts part of String to int */
  public static int ctoi(String s, int start, int len)
  {
    return (int)ctol(s,start,len);
  }
  /* ctol(1) - converts string to long */
  public static long ctol(String s)
  {
    int l = s.length();
    if ( l == 0 ) return 0; // bad assumption!
    int j = s.charAt(0) == '-' ? 1 : 0;
    int i = j;
    long n = 0;
    for (; i < l; i++) n = n*10 + (s.charAt(i)-'0');
    if ( j > 0 ) n = -n;
    return n;
  }
  /* ctol(2) converts part of String to long */
  public static long ctol(String s, int start, int len)
  {
    StringBuffer b = new StringBuffer();
    for (int i=start;len-- > 0;i++) b.append(s.charAt(i));
    return ctol( new String(b) );
  }

  /* edits: edits numeric String */
  public static String edits(String s)
  {
    if (s.length() == 0) return s;
    int p = nfdigit(s);
    StringBuffer b = new StringBuffer(s);
    boolean neg = b.charAt(0) == '-' ? true : false;
    if (neg) b.deleteCharAt(0);
    while ( b.length() > 0 && b.charAt(0) == '0' )
           b.deleteCharAt(0);
    if ( b.length() == 0 ) { // an all-zero String
        b.append(0);
        return new String(b);
    }
    int k = b.length();
    if ( p > 0 ) k = k - p -1;
    while ( k > 3 ) {
          k = k - 3;
          b.insert(k,',');
    }
    if ( b.charAt(0) == '.' ) b.insert(0,'0');
    if (neg) b.insert(0,'-');
    return new String(b);
  }
  /* getmonth */
  public static String getmonth(int n)
  {
    if ( n <= 12 && n >= 1 )
          return mapt( MONTH[n] );
        return MONTH[0];
  }
  /* itis */
  public static boolean itis(String s, char c)
  {
    int l;
    if ( (l=s.length()) == 0) return false;
    int i = 0;
    for (; i < l && c == s.charAt(i); i++ );
    return ( i < l ? false : true);
  }
  /* itoc converts int to String */
  public static String itoc(int i)
  {
    return String.valueOf(i);
  }
        /*-------------------------------
        convert int to String with length
        -------------------------------*/
      /*  public static String itoc(int i, int len)
        {
                return ltoc((long)i, len);
        }*/
        /*----------------------------
        check if current OS is Windows
        ----------------------------*/
        public static boolean isWindows()
        {
                return match(7, System.getProperty("os.name"), 0,
                                            "Windows", 0);
        }
        public static boolean isLinux()
        {
                return match(5, System.getProperty("os.name"), 0,
                                            "Linux", 0);
        }
        public static boolean isMac()
        {
                return match(3, System.getProperty("os.name"), 0,
                                            "Mac", 0);
        }
  /* len returns first position of trailing characters c */
  public static int len(String s, char c)
  {
    int l = s.length();
        for (--l; l >= 0 && c == s.charAt(l); l--)
           ;
        return ++l;
  }
  /* lshift left-logical shift String */
  public static String lshift(String s, int n, char c)
  {
    if ( n > s.length() ) return s;
    StringBuffer b = new StringBuffer(s);
        while (n-- > 0) {
              b.deleteCharAt(0);
                  b.append(c);
        }
        return new String(b);
  }
  /* ltoc converts long to String */
  public static String ltoc(long l)
  {
    return String.valueOf(l);
  }
  /* ----------------------------
     integer to string conversion
     fixed length of output
     ---------------------------- */
 /* public static String ltoc(long n, int len)
  {
        char[] cs = new char[len];
        boolean minus = n < 0;
        int first;
        if (minus)
        {
            n = -n;
            first = 1;
        }
        else
            first = 0;

        for (int i = len-1; i >= first; i--)
        {
             cs[i] = (char)(n % 10 + '0');
             n /= 10;
        }
        if (n > 0)
            ErrorBox.trap(1, "M.ltoc: integer too big");
        if (minus)
            cs[0] = '-';
        return new String(cs);
    }*/
  /* mapt - char mapt(char c) */
  public static char mapt(char c)
  {
    return Ms.mapt(c);
  }
  /* mapt */
  public static String mapt(String e)
  {
    return Ms.mapt(e);
  }
  /* nfdigit - returns no. of fractional digits */
  public static int nfdigit(String s)
  {
    int i = s.indexOf('.');
    return i < 0 ? 0 : s.length()-(++i);
  }

  /* endot - encode dot */
  public static String endot(String s, int p)
  {
    if ( p <= 0 ) return s;
    StringBuffer b = new StringBuffer(s);
    boolean sign = false;
    if ( b.length() > 0 )
      if ( b.charAt(0) == '-' ) sign = true;
    if (sign) b.deleteCharAt(0);
    while ( b.length() < p ) b.insert(0,0);
    b.insert(b.length()-p,'.');
    if (sign) b.insert(0,'-');
    return new String(b);
  }
  /* undot - decode dot */
  public static String undot(String s)
  {
    int i = s.indexOf('.');
    if ( i < 0 ) return s; // no radix point exists
    StringBuffer b = new StringBuffer(s);
    b.deleteCharAt(i);
    return new String(b);
  }
  /* replace */
  public static String replace(String s,char sc,char dc, int how)
  {
    int l;
    if ( ( l = s.length() ) == 0 ) return s;
        StringBuffer b = new StringBuffer(s);
        // how == 0 ==> replace all
        if ( how == 0 ) {
           for (int i = 0; i < l; i++)
              if ( b.charAt(i) == sc ) b.setCharAt(i,dc);
           return new String(b);
        }
        // how < 0 ==> replace leading
        if ( how < 0 ) {
           for (int i = 0; i < l && b.charAt(i) == sc; i++)
               b.setCharAt(i,dc);
           return new String(b);
        }
        // how > 0 ==> replace trailing
        if ( how > 0 ) {
           for (int i = --l; i >= 0 && b.charAt(i) == sc; i--)
               b.setCharAt(i,dc);
           return new String(b);
        }
        return s;
  }
  /* rshift right-logical shift String */
  public static String rshift(String s, int n, char c)
  {
    int l = s.length();
    if (n > l) return s;
        StringBuffer b = new StringBuffer(s);
        l--;
        while (n-- > 0) {
          b.deleteCharAt(l);
                  b.insert(0,c);
        }
        return new String(b);
  }
  /* setlen - adjust numeric string to specified length */
  public static String setlen(String s, int slen)
  {
    if (slen <= 0) return s;
    int p = nfdigit(s);
    StringBuffer b = new StringBuffer(undot(s));
    boolean sign = false;
    if ( b.length() > 0 )
      if ( b.charAt(0) == '-' ) sign = true;
    int j = sign ? 1 : 0;
    while (b.length() < slen) b.insert(j,0);
    while (b.length() > slen) b.deleteCharAt(0);
    return endot(new String(b),p);
  }

  /* strmove(1)- moves bytes between byte arrays */
  public static void strmove(int n, byte[]s, int si,
                                    byte[]d, int di )
  {
    while(n-- > 0) d[di++] = s[si++];
  }
  /* strmove(2)*/
  public static String strmove(int n, String s, int si,
                                      String d, int di)
  {
    int sn = s.length()-si;
    if ( sn < n ) return d;
    int dn = d.length()-di;
    if ( n > dn ) return d;
    StringBuffer b = new StringBuffer(d);
    while (n-- > 0) b.setCharAt( di++, s.charAt(si++) );
    return new String(b);
  }
  /* numeric() - determines a (signed) numeric String */
  public static boolean numeric(String s)
  {
    return Ms.numeric(s);
  }
  /* match(1) - String matching */
  public static boolean match(int n, String s, int si,
                                     String d, int di)
  {
    for (; n-- > 0; ) {
            if ( s.charAt(si++) == d.charAt(di++) ) continue;
                else return false;
        }
        return true;
  }
  /* match(2) */
  public static boolean match(String s, int si, String d)
  {
    int n = d.length();
    return match(n,s,si,d,0);
  }
  /* dateok if YYYYMMDD|YYMMDD is a valid date */
  public static boolean dateok(String s)
  {
    int l = s.length();
    /* calculate year */
    int yl; // size of YYYY|YY
    if (l == 8) yl = 4;
    else if (l == 6) yl = 2;
         else return false;
    int y = ctoi(s,0,yl)-543; // A.D. year
    /* calculate month */
    int m = ctoi(s,yl,2);
    /* claculate day */
    int d = ctoi(s,yl+2,2);
    boolean leap = y%4 == 0 && y%100 != 0 || y%400 == 0;
    return
    (d > 0 && m==2 && leap  && d <=29) ||
    (d > 0 && m==2 && !leap && d <= 28)||
    (d > 0 && (m==4 || m==6 || m==9 || m==11) && d <=30 )||
    (d > 0 && (m==1 || m==3 || m==5 || m==7 ||
               m==8 || m==10|| m==12) && d <=31 );
  }
        // ---------- Font type ---------------------
        public static final char FT_BARCODEA    = 'A';          // barcode type AB
        public static final char FT_BARCODES    = 'a';          // barcode type S
        public static final char FT_BARCODEC    = 'C';          // barcode type C
        public static final char FT_SCREEN              = 'D';          // default, screen
        public static final char FT_SCREENBOLD          = 'B';          // bold, screen
        public static final char FT_SCREENITALIC        = 'I';          // italic, screen
        public static final char FT_SCREENBOLDITALIC  = 'T';          // bold italic, screen
        public static final char FT_PRINTER             = 'P';          // printer
        
        private static Font prtfont = null;
        private static Font deffont = null;
        private static Font boldfont = null;
        private static Font italicfont = null;
        private static Font bolditalicfont = null;
        private static Font bcafont = null;
        private static Font bcsfont = null;
        private static Font bccfont = null;
        // -------------
        // standard font
        // -------------
       /* public static Font stdFont(char type)
        {
                Font font   = null;
                String name = classPath("manit");
                switch (type)
                {
                        case FT_BARCODEA                : name += "bcat.cf";    break;
                        case FT_BARCODES                : name += "bca.cf";             break;
                        case FT_BARCODEC                : name += "bcct.cf";    break;
                        case FT_PRINTER                 : name += "print.cf";   break;
                        case FT_SCREENBOLD              : name += "manitb.cf";  break;
                        case FT_SCREENBOLDITALIC  : name += "manitbi.cf";  break;
                        case FT_SCREENITALIC            : name += "maniti.cf";  break;
                        default                 : name += "manit.cf";   break;
                }
                try
                {
                        FileInputStream fin = new FileInputStream(name);
                        GZIPInputStream file = new GZIPInputStream(fin);
                        font = Font.createFont(Font.TRUETYPE_FONT, file);
                        file.close();
                }
        catch (FileNotFoundException e)
                {       ErrorBox.trap(2, "M.stdFont: "+name+", not found");             }
                catch (FontFormatException e)
                {       ErrorBox.trap(3, "M.stdFont: "+name+", invalid format");        }
                catch (IOException e)
                {       ErrorBox.trap(4, "M.stdFont: "+name+", read failed");   }
        return font;
        }  */     
        // ----------------
        // get default font
        // ---------------- 
     /*   public static Font getFont()
        {
        if (deffont == null)
                        deffont = stdFont(FT_SCREEN).deriveFont(20.1f);
                return deffont;
        }
        // --------------------
        // get screen italic font
        // -------------------- 
        public static Font italicFont()
        {
        if (italicfont == null)
                                        italicfont = stdFont(FT_SCREENITALIC).deriveFont(20.0f);
                        return italicfont;
        }
        // --------------------
        // get screen bold font
        // -------------------- 
        public static Font boldFont()
        {
        if (boldfont == null)
                                        boldfont = stdFont(FT_SCREENBOLD).deriveFont(20.0f);
                        return boldfont;
        }
        // ---------------------------
  // get font with bold and italic style
  // ---------------------------

        public static Font boldItalicFont()
        {
                        if (bolditalicfont == null)
          bolditalicfont = stdFont(FT_SCREENBOLDITALIC).deriveFont(20.0f);
      return bolditalicfont;
                        
        }
        // ----------------
        // get printer font
        // ----------------
        public static Font prtFont()
        {
                if (prtfont == null)
                        prtfont = stdFont(FT_PRINTER).deriveFont(28.5f);
                return prtfont;
        }
        // -------------------
        // get barcode AB font
        // -------------------
        public static Font bcaFont()
        {
                if (bcafont == null)
                        bcafont = stdFont(FT_BARCODEA).deriveFont(20f);
                return bcafont;
        }
        // -------------------------
        // get barcode AB small font
        // -------------------------
        public static Font bcsFont()
        {
                if (bcsfont == null)
                        bcsfont = stdFont(FT_BARCODES).deriveFont(30f);
                return bcsfont;
        }
        // ------------------
        // get barcode C font
        // ------------------
        public static Font bccFont()
        {
                if (bccfont == null)
                        bccfont = stdFont(FT_BARCODEC).deriveFont(20f);
                return bccfont;
        }*/
        // ---------------------------
        // get default font with style
        // ---------------------------
     /*   public static Font getFont(int style)
        {
                Font font ;
                if (style == Font.BOLD)
                {
                                font = boldFont();
                                font = font.deriveFont(Font.BOLD);
                }
                else if (style == Font.ITALIC)
                {
                        font = italicFont();
                        font = font.deriveFont(Font.ITALIC);
                }
                else 
                        font = getFont(); 
                
                return  font;

        }*/
        // -------------
        // set font size
        // ------------- 
        public static Font fontSize(Font font, int size)
        {
                return font.deriveFont((float)size);
        }

  /* inc - increment unsigned numeric string */
  public static String inc(String s)
  {
    StringBuffer b = new StringBuffer(s);
    int i = b.length()-1;
    while (i>=0 && b.charAt(i)=='9') b.setCharAt(i--,'0');
    if ( i >= 0 ) b.setCharAt(i,(char)(b.charAt(i)+1));
    return new String(b);
  }
  /* dec - decrement unsigned numeric string */
  public static String dec(String s)
  {
    StringBuffer b = new StringBuffer(s);
    int i = b.length()-1;
    while (i>=0 && b.charAt(i)=='0') b.setCharAt(i--,'9');
    if (i >= 0) b.setCharAt(i,(char)(b.charAt(i)-1));
    return new String(b);
  }

  /* PRIVATE inrange() */
  private static boolean inrange(int lo, byte b, int hi)
  {
    int v = b & 0xff;
    return ( v >= lo && v <= hi ) ? true : false;
  }

  /* PRIVATE extract */
  private static StringBuffer extract(StringBuffer b,int i,int len)
  {
    StringBuffer t = new StringBuffer();
    while (len-- > 0) t.append(b.charAt(i++));
    return t;
  }
  /* nextdate: date YYYYMMDD or YYMMDD */
  public static String nextdate(String date, int day)
  {
    int l = date.length();
    int y = 0;
    if (l == 8)
      y = ctoi(date,0,4);
    else
      if (l == 6)
        y = ctoi(date,0,2) + 2500;
      else
        return date; /* automatic error return */
    int m = ctoi(date,l-4,2);
    int d = ctoi(date,l-2,2) + day;
    int x = 0;
    if (day > 0)
       while (true) {
             switch (m) {
                case 2:
                     x = (y-543) % 4 == 0 ? 29 : 28;
                     break;
                case 4:case 6:case 9:case 11:
                     x = 30;
                     break;
                default:
                     x = 31;
             } /* switch(m) */
             if ( d > x ) {
                d -= x;
                ++m;
                if ( m > 12 ) {
                   m = 1;
                   ++y;
                }
             }
             else break;
       } /* while true */
    else /* day <= 0 */
      while ( d < 1 ) {
            if ( --m < 1 ) {
               m = 12;
               --y;
            }
            switch (m) {
               case 2:
                    d += (y-543) % 4 == 0 ? 29 : 28;
                    break;
               case 4:case 6:case 9:case 11:
                    d += 30;
                                        break;
               default:
                    d += 31;
                                        break;
            }
      } /* while */
    StringBuffer t = new StringBuffer();
    if (l == 6) {
       if (y >= 2500)
         y = y - 2500;
       else
         y = y - 2400;
       if ( y < 10 ) t.append(0);
    }
    t.append(y);
    if (m < 10) t.append(0);
    t.append(m);
    if (d < 10) t.append(0);
    t.append(d);
    return new String(t);
  }
  /* ntot : left-pad a numeric String with 0's into a 12-digit */
  /* String, then, partitions the 12-digit String into 4 parts */
  /* t1,t2,t3,t3 - each part consists of 3-digit String        */
  /* xxx,xxx,xxx,xxx = 12-digit String                         */
  /* 012,345,678,9 */
  public static String ntot(String s)
  {
    StringBuffer b = new StringBuffer(s);
    while ( b.length() < 12 ) b.insert(0,'0');
    StringBuffer o = new StringBuffer();
    StringBuffer t = extract(b,0,3);
    o.append( l(t) );
    t = extract(b,3,3);
    o.append( r(t) );
    if (o.length() > 0) o.append( mapt(MILLION) );
    t = extract(b,6,3);
    o.append( l(t) );
    t = extract(b,9,3);
    o.append( r(t) );
    return new String(o);
  }
  /* ntot(s,flag) - flag >  0 ==> BAHT, flag <= 0 ==> STANG */
  public static String ntot(String s, int flag)
  {

    StringBuffer b = new StringBuffer();
    b.append( ntot(s) );
    if (flag > 0) // BAHT
      b.append( mapt(BAHT) );
    else // flag <= 0 STANG
      b.append( mapt(STANG) );
    return new String(b);
  }
  /* PRIVATE l */
  private static StringBuffer l(StringBuffer b)
  {
    // b.length() == 3
    StringBuffer o = new StringBuffer();
    for ( int i = 0; i < 3; i++ ) {
        int j = (int) b.charAt(i);
        if ( j != 48 ) {
           j = j - 48 - 1;
           o.append( mapt(N[j]) ).append( mapt(M[i]) );
        }
    }
    return o;
  }
  /* PRIVATE r */
  private static StringBuffer r(StringBuffer b)
  {
    // b.length() == 3
    StringBuffer o = new StringBuffer();
    int i = 0;
    while (true)
    {
      int d = (int) b.charAt(i);
      switch (i) {
         case 0: if ( d == 48 ) {
                    i++;
                    break;
                 }
                 d = d - 48 - 1;
                 o.append( mapt(N[d]) );
                 o.append( mapt(HUNDRED) );
                 i++;
                 break;
         case 1: if ( d == 48 ) {
                    i++;
                    break;
                 }
                 if ( d == 49 ) {
                    o.append( mapt(TEN) );
                    i++;
                    break;
                 }
                 if ( d == 50 ) {
                    o.append( mapt(TWO) ).append( mapt(TEN) );
                    i++;
                    break;
                 }
                 d = d - 48 - 1;
                 o.append( mapt(N[d]) ).append( mapt(TEN) );
                 i++;
                 break;
         case 2: if ( (int)b.charAt(i-1) == 48 ) {
                    d = d - 48;
                    if ( d > 0 ) {
                       d--;
                       o.append( mapt(N[d]) );
                    }
                    return o;
                 }
                 if ( d == 48 ) return o;
                 if ( d == 49 ) {
                    o.append( mapt(ONE) );
                    return o;
                 }
                 if ( d != 48) {
                    d = d - 48 - 1;
                    o.append( mapt(N[d]) );
                    return o;
                 }
      } // switch(i)
    } // while true
  }
  /* sysdate() returns String YYYYMMDD */
  public static String sysdate()
  {
    Calendar c = Calendar.getInstance();
    int d = c.get(Calendar.DATE);
    int m = c.get(Calendar.MONTH) + 1;
    int y = c.get(Calendar.YEAR) + 543;
    StringBuffer b = new StringBuffer();
    b.append(y);
    if ( m < 10 ) b.append(0);
    b.append(m);
    if ( d < 10 ) b.append(0);
    b.append(d);
    return new String(b);
  }
  /* systime returns String HHMMSS */
  public static String systime()
  {
    Calendar c = Calendar.getInstance();
    int h = c.get(Calendar.HOUR_OF_DAY);
    int m = c.get(Calendar.MINUTE);
    int s = c.get(Calendar.SECOND);
    StringBuffer b = new StringBuffer();
    if ( h < 10 ) b.append(0);
    b.append(h);
    if ( m < 10 ) b.append(0);
    b.append(m);
    if ( s < 10 ) b.append(0);
    b.append(s);
    return new String(b);
  }
  /* utok -- UNICODE char to KU byte */
  public static byte utok(char u)
  {
    char s = toscode(u);
    byte k = stok( (byte)(s & 0xff ) );
    return k;
  }
  /* -- char toscode(UNICODEchar) -- */
  private static char toscode(char u)
  {
    if (ASCII_SI) return u;
    if (u >= 0xe01) return (char)(u-0xe01+161);
    return u;
  }
  /* ---- char toucode(SIchar)---- */
  private static char toucode(char c)
  {
    if (ASCII_SI) return c;
                if ( c >= 0xe01) return c;
    else if ( c >= 161 ) return (char)(c-161+0xe01);
    else return c;
  }
  /* ---- is1, is2, is3, is4 ---- */
  /* is1():checks if a Thai char is Type-1 */
  public static boolean is1(char c)
  {
    return  c >= stou((char)232) && c <= stou((char)236)
         ? true : false ;
  }
  /* is2(): checks if a Thai char is Type-2 */
  public static boolean is2(char c)
  {
    return  c == stou((char)209)
            ||(c >= stou((char)212) && c <= stou((char)215))
            || c == stou((char)231)
            ? true : false;
  }

  /* is3:checks if a Thai char is Type-3 */
  public static boolean is3(char c)
  {
    return !is1(c) && !is2(c) && !is4(c) && ( c > 31)
        && (c != stou((char)218)) && (c != stou((char)237))
        ? true : false ;
  }

  /* is4:checks if a Thai char is Type-4 */
  public static boolean is4(char c)
  {
    return  c >= stou((char)216) && c <= stou((char)217)
         ? true : false;
  }
  public static int ntype3(String s)
  {
    int n = 0;
    for (int i = 0; i < s.length(); i++)
        if ( is3( s.charAt(i) ) ) n++;
    return n;
  }

    private static String fsep;
    private static String psep;
    private static String path;
    /*  ------------------------
                get some system property
                ------------------------ */
    private static void getProp()
    {
        if (fsep != null)
           return;

        Properties p = System.getProperties();
        path = p.getProperty("java.class.path");
        psep = p.getProperty("path.separator");
        fsep = p.getProperty("file.separator");
    }
    /*  -------------------------------------
                find path of a directory in classpath
                ------------------------------------- */
    public static String classPath(String dir)
    {
        getProp();
        StringTokenizer st = new StringTokenizer(path, psep);
        File file;
        String s;
        while (st.hasMoreTokens())
        {
            s = st.nextToken();
                        if (s.charAt(s.length()-1) == fsep.charAt(0))
                                s += dir;
                        else
                                s += fsep + dir;
            file = new File(s);
            if (file.isDirectory())
                return s + fsep;
        }
        return null;
    }
    /*  ----------------------
                get filename separator
                ---------------------- */
    public static String separator()
    {
        getProp();
        return fsep;
    }
        /*  -----------------------------------
                citizen id. check digit calculation
                ----------------------------------- */
        private static char cdcalc(String cid)
        {
                int cnt = 13;
                int sum = 0;
                for (int i = 0; i < 12; i++, cnt--)
                        sum += (cid.charAt(i) - '0') * cnt;
                sum = (11 - (sum % 11)) % 10;
                sum += '0';
                return (char)sum;
        }
        /*  ----------------------------------------------
                check citizen id.
                 0     - valid id 
                -1     - invalid format
                 other - check digit not equal, value returned
                                 is the program calculated check char.
                ---------------------------------------------- */
        public static int cidcheck(String cid)
        {
                if (cid.length() != 13)
                        return -1;
                if ( ! numeric(cid))
                        return -1;
                if (cid.charAt(0) == '-') // minus numeric
                        return -1;
                char ch = cdcalc(cid);
                if (ch != cid.charAt(12))
                        return (int)ch;
                return 0;
        }
        //      -------------
        //      get user name
        //      -------------
        public static String userName()
        {
                return System.getProperty("user.name");
        }

        /* ntoe numeric string to englist word */
        private static final String[] lowNames = {
        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
    "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};

  private static final String[] tensNames = {
        "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

  private static final String[] bigNames = {
        "thousand", "million", "billion"};

        public static String ntoe(String n) {
        int l = n.indexOf(".");
    if (l < 0)
        return "BAHT " + convertNumberToWords(Long.parseLong(n)).toUpperCase() + " ONLY";
    else {
        String b = "BAHT " + convertNumberToWords(Long.parseLong(n.substring(0,l))).toUpperCase();
      String s = n.substring(l+1);
      if (s.length() == 3)
        return "ERROR STANG : LENGTH > 2";
      if (s.equals("0") || s.equals("00"))
        return b + " ONLY";
      else {
        s = "SATANG " + convertStang(n.substring(l+1)).toUpperCase();
        return b + " AND " + s + " ONLY";
      }
    }
        }

        private static String convertNumberToWords(long n) {
        if (n < 0) {
        return "error minus " + convertNumberToWords(-n); }
    if (n <= 999) {
        return convert3digits(n); }
    String s = null;
    int t = 0;
    while (n > 0) {
        if (n % 1000 != 0) {
        String s2 = convert3digits(n % 1000);
        if (t > 0) {
                s2 = s2 + " " + bigNames[t-1]; }
        if (s == null) {
                s = s2; }
        else {
                s = s2 + " " + s; }}
      n /= 1000;
      t++; }
    return s;
    }

                private static String convert3digits (long n) {
        String s1 = lowNames[(int) (n / 100)] + " hundred";
      String s2 = convert2digits(n % 100);
      if (n <= 99) {
        return s2; }
      else if (n % 100 == 0) {
        return s1; }
      else {
        return s1 + " " + s2; }
    }

                private static String convert2digits (long n) {
        if (n < 20) {
        return lowNames[(int) n]; }
      String s = tensNames[(int) (n / 10 - 2)];
      if (n % 10 == 0) {
        return s; }
      return s + " " + lowNames[(int) (n % 10)];
    }

                private static String convertStang(String s) {
        int l     = s.length();
      long s1   = Long.parseLong(s);
      String s0 = "ERROR LENGTH > 2";
      if (l < 2) {
        if (s1 == 0)
                return lowNames[0];
        if (s1 == 1)
                return lowNames[10];
        return tensNames[(int) (s1 - 2)];
      } else if (l == 2) {
        if (s1 <= 99)
                return convert2digits(s1 % 100);
      }
      return s0;
    }

} /* class M */
