package com.luis.reflejovision.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.Part;
import javax.swing.Icon;

import com.luis.reflejovision.model.Usuario;

public interface FileService {
	
	public BufferedImage scaleImage(File imageFile, int targetWidth, int targetHeight) throws IOException;
	
	public BufferedImage createThumbnail(BufferedImage image, int width, int height);
	
	public Icon scaleIcon(Icon icon, int width, int height);
	
	public Image iconToImage(Icon icon);
	
	public List<File> getImagesByBookId(Long usuarioId);
	//BufferedImage
	public void uploadImages(Usuario usuario, List<File> selectedFiles);
	
	public List<File> getProfileImageByUsuarioId(Long usuarioId);
	
	public void uploadUserProfileImage(Long usuarioId, byte[] fileBytes);
}
