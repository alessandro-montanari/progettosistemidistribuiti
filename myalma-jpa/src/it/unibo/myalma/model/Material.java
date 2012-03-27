package it.unibo.myalma.model;

import it.unibo.myalma.model.Content;

import java.io.File;
import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.EnumType.STRING;

/**
 * Entity implementation class for Entity: Material
 *
 */
@Entity
@Access(FIELD)
@DiscriminatorValue("material")
public class Material extends Content implements Serializable {

	private String path;
	private long size;
	
	
	@Enumerated(STRING)
	private MaterialFormat format;
	
	private static final long serialVersionUID = 1L;

	public Material() {
		super();
	}   
	
	public Material(User creator)
	{
		this("","","",creator);
	}
	
	public Material(String title, String path, String description, User creator)
	{
		super(ContentType.MATERIAL, title, description, creator);
		this.setPath(path);
	}
	
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
		File file = new File(path);
		this.size = file.length();
		String ext = file.getName().substring(file.getName().lastIndexOf(".")+1, file.getName().length());
		if(ext.equals("pdf") || ext.equals("docx") || ext.equals("doc") || ext.equals("txt"))
			format = MaterialFormat.TEXT;
		else if (ext.equals("avi") || ext.equals("mp4"))
			format = MaterialFormat.VIDEO;
		else if (ext.equals("mp3"))
			format = MaterialFormat.AUDIO;
		else
			format = MaterialFormat.OTHER;
	}
	
	public long getSize() {
		return size;
	}
	
	public MaterialFormat getFormat() {
		return format;
	}
}
