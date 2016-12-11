package com.example;

import java.util.ArrayList;

public class Photo {
	
	public String Id;//图片存在数据库里应该以id为唯一主键，比如Author_<Date>_Title.jpg
	public String Title;
	public String Author;
	public ArrayList<String> Tags;
	public String Essay;
	public PhotoDetails photoDetails;
	public PhotoComments[] photoComments;

}
