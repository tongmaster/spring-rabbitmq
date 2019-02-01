package com.example.demo.util;

import java.awt.Font;
public interface MsInterface
{
        public void             error(String s);
        public Font             getFont();
        public int              dialog(String s, int type, int response);
        public String   btou(byte[] bs, int pos, int len);
        public void             utob(String s, byte[] bs, int pos, int len);
        static final long serialVersionUID = 255102220001L ;
}