package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ProcesadorDeImagenes {
	
	private File file;
	private File[] files;
	private String nombreArchivoImagen="";
	private Imagen image;
	private Imagen[] secuenciaImagenes;
	private int umbralOptimo = 0;
			
	public Imagen abrirImagen(boolean esSecuencial){
		String tipoImagen;		
		Imagen BImg=null;
		
		if (esSecuencial){
			JFileChooser selector=new JFileChooser();
			FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("PPM & PGM & BMP & JPG & JPEG & PNG", "ppm", "pgm", "bmp", "jpg", "jpeg", "png");
			selector.setFileFilter(filtroImagen);
			selector.setDialogTitle("Seleccione secuencia de imagenes del mismo tipo y tamaño");
			selector.setMultiSelectionEnabled(true);
			int flag=selector.showOpenDialog(null);
			if(flag==JFileChooser.APPROVE_OPTION){
				try {
					this.files = selector.getSelectedFiles();
					// cargo las imagenes seleccionadas
					this.secuenciaImagenes=new Imagen[this.files.length];
					for (int i=0;i<this.files.length;i++){
						tipoImagen=obtenerTipo(files[i]);
						System.out.println(files[i].getName());
						this.secuenciaImagenes[i]=obtenerImagen(tipoImagen, files[i]);
					}
					// obtengo info de la primer imagen
					this.file = this.files[0];
					this.nombreArchivoImagen=this.file.getName();
					BImg = this.secuenciaImagenes[0];
				} catch (Exception e) {
					System.out.println("ERROR DE APERTURA SECUENCIA DE IMAGENES");
				}
			}
		} else {
			JFileChooser selector=new JFileChooser();
			FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("PPM & PGM & BMP & JPG & JPEG & PNG", "ppm", "pgm", "bmp", "jpg", "jpeg" ,"png");
			selector.setFileFilter(filtroImagen);
			selector.setDialogTitle("Seleccione una imagen");
			selector.setMultiSelectionEnabled(false);
			int flag=selector.showOpenDialog(null);
			if(flag==JFileChooser.APPROVE_OPTION){
				try {
					file=selector.getSelectedFile();
					tipoImagen=obtenerTipo(file);
					this.nombreArchivoImagen=file.getName();
					System.out.println(file.getName());
					BImg = obtenerImagen(tipoImagen, file);
				} catch (Exception e) {
					System.out.println("ERROR DE APERTURA DE ARCHIVO: " + nombreArchivoImagen);
				}
			}
		}
			
		this.image = BImg;
		return BImg;
	}

	public Imagen obtenerImagen(String tipoImagen, File file) throws IOException {
		Imagen image = null;
		Procesador proc = null;
		if ((tipoImagen.equalsIgnoreCase("BMP"))||(tipoImagen.equalsIgnoreCase("JPG"))||(tipoImagen.equalsIgnoreCase("JPEG")||(tipoImagen.equalsIgnoreCase("PNG")))){
			BufferedImage image2 = ImageIO.read(file);
			ProcesadorDeImagenesJPGyBMP proc2 = new ProcesadorDeImagenesJPGyBMP(image2);
			image = proc2.getImage();
			
		} else{ 
			if ((tipoImagen.equalsIgnoreCase("PPM"))){
				proc = new ProcesadorDeImagenesPPM();
				image = proc.abrirImagen(file.getPath());				
			}else { 
				if ((tipoImagen.equalsIgnoreCase("PGM"))){
					proc = new ProcesadorDeImagenesPGM();
					image = proc.abrirImagen(file.getPath());
				} 		
			} 
		}
		return image;
	}

	private String obtenerTipo(File file) {
		String nombre = file.getName();
		int posPunto = nombre.lastIndexOf(".", nombre.length());
		int indexOfExtension = posPunto+1;
		return  nombre.substring(indexOfExtension).toUpperCase();
		
	}

	public Imagen getImagen() {
		if (image != null)
			return image;
		return null;
	}
		
	public String getNombreArchivoImagen() {
		return nombreArchivoImagen;
	}

	public void setNombreArchivoImagen(String nombreArchivoImagen) {
		this.nombreArchivoImagen = nombreArchivoImagen;
	}
	
	public void setImagen(Imagen image1) {
		this.image=image1;
	}

	public Imagen umbralOtsu(Imagen buff) {
		Imagen salida=null;
		int umbralOptimo=0;
		double gw=0;
		double gwMaximo=0;
		Color blanco=new Color(255,255,255);
		Color negro=new Color(0,0,0);
		if (buff!=null){
			int pixeles= buff.getHeight()*buff.getWidth();
		    salida= new Imagen(buff.getWidth(), buff.getHeight());	
			int[] histograma = histograma();
			double[] ocurrencia= new double[256];
			for (int i=0; i < 256; i++){
				ocurrencia[i]=(double) histograma[i]/pixeles;		
			}
			gwMaximo=calculoVarianza(0,ocurrencia);
			for(int umbral =1; umbral < 256; umbral++){
				gw=calculoVarianza(umbral,ocurrencia);
				if(gw > gwMaximo){
					gwMaximo=gw;
					umbralOptimo=umbral;
				}	
			}
			this.umbralOptimo = umbralOptimo; 
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					if(calcularPromedio(buff.getRGB(i, j)) >= umbralOptimo){
						salida.setRGB(i, j, blanco.getRGB());
					}else{
						salida.setRGB(i, j, negro.getRGB());
					}
				}
			}
		}
		return salida;
	}
	
	@SuppressWarnings("javadoc")
	public int valorUmbralOtsu(){
		return umbralOptimo;
	}
	
	private double calculoVarianza(int umbral, double[] ocurrencia){
		double w1=0;
		double w2=0;
		double u1=0;
		double u2=0;
		double ut=0;
		double gb=0;
		for(int i =0; i < 256; i++){
			if(i<umbral){
				w1+=ocurrencia[i];
			}else{
				w2+=ocurrencia[i];
			}
		}
		for(int i =0; i < 256; i++){
			if(i<umbral){
				u1+=i*ocurrencia[i];
			}else{
				u2+=i*ocurrencia[i];
			}
		}
		if(w1!=0){
			u1=(double) u1/w1;
		}
		if(w2!=0){
			u2=(double) u2/w2;
		}
		ut=w1*u1 + w2*u2;
		gb=w1*Math.pow(u1-ut,2) + w2*Math.pow(u2-ut,2);	
		return gb;
	}
	
	public int[] histograma(){
        int histograma[]=new int[256];
        for( int i = 0; i < this.image.getWidth(); i++ ){
            for( int j = 0; j < this.image.getHeight(); j++ ){
                histograma[calcularPromedio(this.image.getRGB(i, j))]+=1;

            }
        }
        return histograma;
    }
	
	private int calcularPromedio(int rgb) {
		int promedio;
		Color c = new Color(rgb);
		promedio = (int)((c.getBlue()+c.getGreen()+c.getRed())/3);
		return promedio;
	}	

	public Imagen canal(int canal, Imagen buff){
		Imagen salida = new Imagen(buff.getWidth(),buff.getHeight());
		Color color;  
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				color = obtenerCanal(canal,buff.getRGB(i, j));
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;
	}
	
	private Color obtenerCanal(int canal, int rgb) {
		Color color;
		color = new Color(rgb);
		int c;
		if (canal==1){
			c = color.getRed();
			color = new Color(c,0,0);
		}else{
			if (canal==2){
				c = color.getGreen();
				color = new Color(0,c,0);
			}else{
				color = new Color(rgb);
				c = color.getBlue();
				color = new Color(0,0,c);
			}
		}
		return color;
	}

	public Imagen getImage() {
		return image;
	}
	
	public Imagen[] getSecuenciaImagenes() {
		return secuenciaImagenes;
	}
}