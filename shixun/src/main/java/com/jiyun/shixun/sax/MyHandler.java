package com.jiyun.shixun.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by 张萌 on 2017/4/6.
 */
public class MyHandler  extends DefaultHandler {
    private ArrayList<Person>  mList=new ArrayList<>();
    private String  mName;
    private Person  mP;

    public ArrayList<Person>  getList(){
        return  mList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        mName=qName;
        if(mName.equals("news")){
            mP=new Person();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(qName.equals("news")){
            mList.add(mP);
            mP=null;
        }
        mName="";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        String  str=new String(ch,start,length);
        if(mName.equals("id")){
            mP.setId(str);
        }else if(mName.equals("title")) {
            mP.setTitle(str);
        }else if(mName.equals("body")){
            mP.setBody(str);
        }
    }
}
