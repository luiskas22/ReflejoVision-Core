package com.luis.reflejovision.service.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.PinguelaException;
import com.luis.reflejovision.conf.ConfigurationParametersManager;
import com.luis.reflejovision.model.Usuario;
import com.luis.reflejovision.service.FileService;
import com.luis.reflejovision.service.UsuarioService;


public class FileServiceImpl implements FileService{
	
	private static final String BASE_PATH = "base.image.path";
	private static final String BASE_PROFILEIMAGE_PATH = "base.profile.image.path";
	
	private static Logger logger = LogManager.getLogger(FileServiceImpl.class);
	private UsuarioService usuarioService = new UsuarioServiceImpl();

	
	@Override
	public BufferedImage createThumbnail(BufferedImage image, int width, int height) {
		
		Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

		return bufferedImage;
	}
	
	
	 @Override
	    public BufferedImage scaleImage(File imageFile, int targetWidth, int targetHeight) throws IOException {
	        BufferedImage originalImage = ImageIO.read(imageFile);
	        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

	        // Escalado suave para una mejor calidad
	        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

	        // Convertir la imagen escalada a BufferedImage
	        BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, type);
	        bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

	        return bufferedImage;
	    }
	
	public Image iconToImage(Icon icon) {
	    if (icon instanceof ImageIcon) {
	        return ((ImageIcon) icon).getImage();
	    } else {
	        int w = icon.getIconWidth();
	        int h = icon.getIconHeight();
	        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	        Graphics g = image.getGraphics();
	        icon.paintIcon(null, g, 0, 0);
	        g.dispose();
	        return image;
	    }
	}

	public Icon scaleIcon(Icon icon, int width, int height) {
	    Image image = iconToImage(icon);
	    Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaledImage);
	}

//	public List<File> getImagesByBookId(Long usuarioId) {
//		 List<File> imageFiles = new ArrayList<>();
//		    try {
//		       
//		        LibroDTO libro = libroService.findByLibro(libroId);
//		        Idioma idioma = idiomaService.findById(libro.getIdiomaId());
//
//		       
//		        File folder = new File(ConfigurationParametersManager.getParameterValue(BASE_PATH)+ File.separator + libro.getId() + File.separator + idioma.getIso639().toUpperCase());
//
//		       
//		        File[] filesInFolder = folder.listFiles();
//
//		        if (filesInFolder != null) {
//		            for (File file : filesInFolder) {
//		                if (file.isFile() && file.getName().matches("g\\d+\\.(jpg|png)")) {
//		                    imageFiles.add(file);
//		                }
//		            }
//		        }
//		    } catch (Exception e) {
//		        logger.error(e.getMessage(), e);
//		    }
//		    return imageFiles;
//	}
	
	public List<File> getProfileImageByUsuarioId(Long usuarioId) {
		
		List<File> images = new ArrayList<File>();
		
		Usuario usuario = new Usuario();
		
		try {
			usuario = usuarioService.findById(usuarioId);
		}catch(PinguelaException pe) {
			logger.error(pe.getMessage(), pe);
		}
		
		File folder = new File(ConfigurationParametersManager.getParameterValue(BASE_PROFILEIMAGE_PATH)+ File.separator + usuario.getId());
		
		File[] filesInFolder = folder.listFiles();
		if(filesInFolder != null) {
			for(File file : filesInFolder) {
			    if (file.isFile() && file.getName().matches("(?i)g\\d+\\.(jpg|png)")) {
					images.add(file);
				}
			}
		}
		return images;
	}
	
	
	private int getNextImageNumber(File isoFolder) {
		int maxNumber = 0;
		File[] files = isoFolder.listFiles((dir, name) -> name.matches("g\\d+\\.(jpg|png)"));
		if(files != null) {
			for(File file : files) {
				String name = file.getName();
				int number = Integer.parseInt(name.substring(1, name.indexOf('.')));
				if(number > maxNumber) {
					maxNumber = number;
				}
			}
		}
		return maxNumber + 1;
	}


	@Override
	public List<File> getImagesByBookId(Long usuarioId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void uploadImages(Usuario usuario, List<File> selectedFiles) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void uploadUserProfileImage(Long usuarioId, byte[] fileBytes) {
	    String basePath = ConfigurationParametersManager.getParameterValue(BASE_PROFILEIMAGE_PATH);
	    String usuarioPath = basePath + File.separator + usuarioId;

	    // Create user directory if it doesn't exist
	    File directory = new File(usuarioPath);
	    if (!directory.exists() && !directory.mkdirs()) {
	        logger.error("Error al crear el directorio: " + usuarioPath);
	        return;
	    }

	    // Save the file with a fixed or dynamic name
	    String fileName = "g1.jpg"; // Static name or dynamically generate
	    String filePath = usuarioPath + File.separator + fileName;

	    // Write the byte array to the file
	    try {
	        Files.write(new File(filePath).toPath(), fileBytes);
	        logger.info("Archivo guardado en: " + filePath);
	    } catch (IOException e) {
	        logger.error("Error guardando el archivo", e);
	    }
	}



//	@Override
//	public void uploadImages(LibroDTO libro, List<File> selectedFiles) {
//	    if (selectedFiles != null && !selectedFiles.isEmpty()) {
//	        try {
//	            Idioma idioma = idiomaService.findById(libro.getIdiomaId());
//	            
//	            String baseDirectory = ConfigurationParametersManager.getParameterValue(BASE_PATH);
//	            File bookFolder = new File(baseDirectory, libro.getId().toString());
//	            
//	            if (!bookFolder.exists()) {
//	                bookFolder.mkdir();
//	            }
//	            
//	            File isoFolder = new File(bookFolder, idioma.getIso639());
//	            if (!isoFolder.exists()) {
//	                isoFolder.mkdir();
//	            }
//	            
//	            for (File selectedFile : selectedFiles) {
//	                int nextImageNumber = getNextImageNumber(isoFolder);
//	                String newFileName = "g" + nextImageNumber + ".jpg";
//	                
//	                File newFile = new File(isoFolder, newFileName);
//	                
//	                Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//	            }
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	        }
//	    }
//		
//	}
//
//
//	@Override
//	public void uploadImages(Usuario usuario, List<File> selectedFiles) {
//		 if (selectedFiles != null && !selectedFiles.isEmpty()) {
//		        try {
//		            Idioma idioma = idiomaService.findById(libro.getIdiomaId());
//		            
//		            String baseDirectory = ConfigurationParametersManager.getParameterValue(BASE_PATH);
//		            File userFolder = new File(baseDirectory, usuario.getId().toString());
//		            
//		            if (!userFolder.exists()) {
//		                userFolder.mkdir();
//		            }
//		            
//		            File isoFolder = new File(userFolder, idioma.getIso639());
//		            if (!isoFolder.exists()) {
//		                isoFolder.mkdir();
//		            }
//		            
//		            for (File selectedFile : selectedFiles) {
//		                int nextImageNumber = getNextImageNumber(isoFolder);
//		                String newFileName = "g" + nextImageNumber + ".jpg";
//		                
//		                File newFile = new File(isoFolder, newFileName);
//		                
//		                Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//		            }
//		        } catch (Exception e) {
//		            logger.error(e.getMessage(), e);
//		        }
//		    }
//			
//		}

}
