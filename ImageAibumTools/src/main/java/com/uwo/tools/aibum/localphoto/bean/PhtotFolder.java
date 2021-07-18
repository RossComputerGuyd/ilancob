package com.uwo.tools.aibum.localphoto.bean;
import java.io.Serializable;
import java.util.List;
public class PhtotFolder implements Serializable{
private static final long serialVersionUID = 1L;
	private List<AlbumInfo> list;
	public List<AlbumInfo> getList() {
		return list;
	}
	public void setList(List<AlbumInfo> list) {
		this.list = list;
	}
}
