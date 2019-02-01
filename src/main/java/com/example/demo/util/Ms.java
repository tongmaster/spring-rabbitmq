package com.example.demo.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.awt.Font;
// ***************************
// Ms : binding class & method 
// between manit & swt package
// ***************************
public class Ms
{
    public static final int DEFAULT    =  0;
    public static final int YES        =  1;
    public static final int NO         =  2;
    public static final int OK         =  4;
    public static final int CANCEL     =  8;

    public static final int INFO       = 100;
    public static final int QUESTION   = 101;
    public static final int WARN       = 102;
    public static final int ERROR      = 103;

        private static MsInterface      intf;
        private static boolean          gui;
        static final long serialVersionUID = 255102220001L ;
        // ------------------
        // static part for Ms
        // ------------------
        static
        {
                plugIn("manit.MsPlugIn");
        }
        // -----------------------------
        // plug the MsInterface class in
        // -----------------------------
        public static void plugIn(String className)
        {
                Class<?> cfx = getClass(className);
                Constructor<?> cx = cfx.getConstructors()[0];
                intf = (MsInterface)getInstance(cx, null);
        }
        // -------------------------
        // the gui mode is activated
        // -------------------------
        public static void guiOn()
        {
                gui = true;
        }
        // -----------------------
        // check if gui mode is on
        // -----------------------
        public static boolean isGui()
        {
                return gui;
        }
        // +++++++++++++++++++
        // show message & exit
        // +++++++++++++++++++
        private static void exit(String s)
        {
                System.err.println(s);
                System.exit(0);
        }
        // +++++++++++++++++++++++
        // get class Class by name
        // +++++++++++++++++++++++
        private static Class<?> getClass(String className)
        {
                Class<?> cls = null;
                try
                        { cls = Class.forName(className); }
                catch (ClassNotFoundException e)
                        { exit("class <"+className+"> not found"); }
                return cls;
        }
        // +++++++++++++++++++++++++++++++++++
        // get a new instance from constructor
        // +++++++++++++++++++++++++++++++++++
        private static Object getInstance(Constructor<?> ctx, Object[] param)
        {
                Object dlg = null;
                try
                        { dlg = ctx.newInstance(param); }
                catch (InvocationTargetException e)
                        { exit("invalid invocation target"); }
                catch (InstantiationException e)
                        { exit("instantiation failed"); }
                catch (IllegalAccessException e)
                        { exit("illegal access"); }
                catch (IllegalArgumentException e)
                        { exit("illegal argument"); }
                return dlg;
        }
        // ---------------------------------
        // display error message & terminate
        // ---------------------------------
        public static void error(String s)
        {
                intf.error(s);
        }
        // ----------------
        // get default font
        // ----------------
        public static Font getFont()
        {
                return intf.getFont();
        }
        // --------------------------
        // show dialog & get response
        // --------------------------
        public static int dialog(String s, int type, int response)
        {
                return intf.dialog(s, type, response);
        }
        // ------------------------------------
        // convert byte array to unicode String
        // ------------------------------------
        public static String btou(byte[] bs, int pos, int len)
        {
                return intf.btou(bs, pos, len);
        }
        // ------------------------------------
        // convert unicode String to byte array
        // ------------------------------------
        public static void utob(String s, byte[] bs, int pos, int len)
        {
                intf.utob(s, bs, pos, len);
        }
    private static final char[] toThaiTable =
        {
        0x2b, 0x2e, 0xf2, 0xf3, 0xf4, 0xdf, 0xa7, 0xf6,    //  ! " # $ % & ' (
        0xf7, 0xf5, 0xf9, 0xc1, 0xa2, 0xe3, 0xbd, 0xa8,    //  ) * + , - . / 0
        0xe5, 0x2f, 0x2d, 0xc0, 0xb6, 0xd8, 0xd6, 0xa4,    //  1 2 3 4 5 6 7 8
        0xb5, 0xab, 0xc7, 0xb2, 0xaa, 0xcc, 0xc6, 0xf1,    //  9 : ; < = > ? @
        0xc4, 0xef, 0xa9, 0xaf, 0xae, 0xe2, 0xac, 0xe7,    //  A B C D E F G H
        0xb3, 0xeb, 0xc9, 0xc8, 0x3f, 0xec, 0xcf, 0xad,    //  I J K L M N O P
        0xf0, 0xb1, 0xa6, 0xb8, 0xea, 0xce, 0x22, 0x29,    //  Q R S T U V W X
        0xed, 0x28, 0xba, 0xa3, 0xc5, 0xd9, 0xf8, 0x60,    //  Y Z [ \ ] ^ _ `
        0xbf, 0xd4, 0xe1, 0xa1, 0xd3, 0xb4, 0xe0, 0xe9,    //  a b c d e f g h
        0xc3, 0xe8, 0xd2, 0xca, 0xb7, 0xd7, 0xb9, 0xc2,    //  i j k l m n o p
        0xe6, 0xbe, 0xcb, 0xd0, 0xd5, 0xcd, 0xe4, 0xbb,    //  q r s t u v w x
        0xd1, 0xbc, 0xb0, 0xa5, 0x2c                       //  y z { | }
        };
    /* ----------------------------
       map English keyboard to Thai
       ---------------------------- */
    public static char mapt(char c)
    {
                if (c < '!' || '}' < c)
                        return c;
                c = toThaiTable[c-33];
                if (c < 161)
                        return c;
        return (char)(c-161+0xe01);
    }
    /* ----------------------------
       map English keyboard to Thai
       ---------------------------- */
    public static String mapt(String s)
    {
        char[] cs = s.toCharArray();
        int n = 0;
        for (int i = 0; i < cs.length; i++, n++)
            if (cs[i] == '~' && i < (cs.length-1))
            {
                cs[n] = cs[i+1];
                i++;
            }
            else
                cs[n] = mapt(cs[i]);
        return new String(cs, 0, n);
    }
    /* --------------------
       check numeric string
       -------------------- */
    public static boolean numeric(String s)
    {
        int len = s.length();
        char c;
        int numCount = 0;
        boolean dotFound = false;
        for (int i = 0; i < len; i++)
        {
            c = s.charAt(i);
            if (c == '-')
            {
                if (i != 0)
                    return false;
            }
            else if (c == '.')
                if (dotFound)
                    return false;
                else
                    dotFound = true;
            else if (c < '0' || '9' < c)
                return false;
            else
                numCount++;
        }
        return numCount > 0;
    }
        // ---------------------------------
        // check if the string is all digits
        // ---------------------------------
        public static boolean digit(String s)
        {
                int len = s.length();
                int i = 0;
                for (; i < len; i++)
                {
                        char c = s.charAt(i);
                        if (c < '0' || '9' < c)
                                break;
                }
                return i == len;
        }
        // ----------------------------------
        // map to an english character if 
        // input was mapped to thai by system
        // ----------------------------------
        public static char unmapt(char c, int kc)
        {
                char[] toEngTable = 
                {       
                        100,  45,  92,  56, 124,  83,  39,  48,  67,
                         61,  58,  71,  80,  69,  68, 123,  82,  60,
                         73, 102,  57,  53, 109,  84, 111,  91, 120,
                        122,  47, 114,  97,  52,  44, 112, 105,  65,
                         93,  63,  59,  76,  75, 108, 115,  62, 118,
                         86,  79, 116, 121, 107, 101,  98, 117,  55,
                        110,  54,  94,   95,   0,   0,   0,   0,  38,
                        103,  99,  70,  46, 119,  49, 113,  72, 106,
                        104,  85,  74,  78,  89,   0,  66,  81,  64,
                         35,  36,  37,  42,  40,  41,  95,  43
                };
                if (c >= 34 && c <= 63)
                {
                        switch (c)
                        {
                                case 34 : // fun noo
                                        if (kc == 87 || kc == 0) return 'W';
                                        break;
               case 37 :
                    if (kc == 192 || kc == 523 ) return '~' ;
                                        break;
                                case 40 : // open round bracket
                                        if (kc == 90 || kc == 0) return 'Z';
                                        break;
                                case 41 : // close round bracket
                                        if (kc == 88 || kc == 0 ) return 'X';
                                        break;
                                case 43 : // plus sign
                                        if (kc == 49 || kc == 0 ) return '!';
                                        break;
                                case 44 : // comma
                                        if (kc == 93 || kc == 0 ) return '}';
                                        break;
                                case 45 : // minus sign
                                        if (kc == 51 || kc == 0 ) return '3';
                                        break;
                                case 46 : // point
                                        if (kc == 222 || kc == 0) return '\"';
                                        break;
                                case 47 : // slash
                                        if (kc == 50 || kc == 0) return '2';
                                        break;
                                case 63 : // question mark
                                        if (kc == 77 || kc == 0 ) return 'M';
                                        break;
                        }
                }
                if (c < 0xe01)
                        return c;
                int i = c - 0xe01;
                if (i >= 89)
                        return c;       // can not be mapped
                char x = toEngTable[i];
                if (x == 0)
                        return c;       // no map
                return x; 
        }
}