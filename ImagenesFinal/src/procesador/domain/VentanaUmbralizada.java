package procesador.domain;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class VentanaUmbralizada extends javax.swing.JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JScrollPane jScrollPane1;
	
	public VentanaUmbralizada(Imagen imagen) {
		initComponents(imagen);
	}
	
	private void initComponents(Imagen imagen) {
		jScrollPane1 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		int ancho=imagen.getAncho();
		int alto=imagen.getAlto();
		jScrollPane1.setBounds(0, 0, ancho, alto);
		jScrollPane1.setViewportView(contenedorDeImagen);;
		contenedorDeImagen.setVerticalAlignment(SwingConstants.TOP);
		this.setLayout(null);
		this.add(jScrollPane1);
		this.setVisible(true);		
		this.setBounds(150, 50, ancho, alto);
		setTitle("Imagen Umbralizada a Color");
		contenedorDeImagen.setIcon(new ImageIcon(imagen));
	}
}
