package procesador.domain;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class Editor extends javax.swing.JFrame{

	private ProcesadorDeImagenes ObjProcesamiento = new ProcesadorDeImagenes();
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JScrollPane jScrollPane1;
	private JMenuBar menuBar = new JMenuBar();	
	private JMenu menuArchivo = new JMenu("Archivo");
	private JMenuItem itemCargar = new JMenuItem("Abrir Imagen");
	private JMenuItem itemCerrar = new JMenuItem("Cerrar");
	private JMenu menuUmbralizar = new JMenu("Umbralizar");
	private JMenuItem itemUmbralColores = new JMenuItem("Umbralizacion en Colores");
	private JMenuItem itemUmbralVideo = new JMenuItem("Umbralizacion de Video");
	private Imagen[] imagenes;
	private Imagen imagen;
    
	@SuppressWarnings("javadoc")
	public Editor() {
		initComponents();
	}
	
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		this.setJMenuBar(crearMenu());
		definirFuncionCerrar();		
		agregarBotones();		
		this.setBounds(0, 0, 500, 500);
		jScrollPane1.setBounds(0, 0, 500, 500);
		jScrollPane1.setViewportView(contenedorDeImagen);;
		contenedorDeImagen.setVerticalAlignment(SwingConstants.TOP);
		this.setLayout(null);
		this.add(jScrollPane1);
		this.setVisible(true);
	}

	private JMenuBar crearMenu() {
		menuArchivo.add(itemCerrar);
		menuArchivo.add(itemCargar);
		menuArchivo.add(itemCerrar);
		menuBar.add(menuArchivo);
		menuUmbralizar.add(itemUmbralColores);
		menuUmbralizar.add(itemUmbralVideo);
		menuBar.add(menuUmbralizar);
		return menuBar;
	}

	private void definirFuncionCerrar() {
		setTitle("Procesador de Imagenes");
	}

	private void agregarBotones() {
		agregarMenuCerrar();
		agregarMenuCargar();
		agregarUmbralColor();
		agregarUmbralVideo();
	}

	private void agregarUmbralColor() {
		// TODO Auto-generated method stub
		itemUmbralColores.addActionListener(new java.awt.event.ActionListener() {	
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				UmbralColor procesador = new UmbralColor();
				Imagen resultado = procesador.umbralizar(imagen);
				new VentanaUmbralizada(resultado);
			}
		});
	}

	private void agregarUmbralVideo() {
		// TODO Auto-generated method stub
		itemUmbralVideo.addActionListener(new java.awt.event.ActionListener() {	
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ObjProcesamiento.abrirImagen(Boolean.TRUE);
				imagenes = ObjProcesamiento.getSecuenciaImagenes();			
				if ((imagenes!=null)&&(imagenes.length>=1)){
					
					new ProcesadorDeVideo(imagenes);
						
				}else{
					JOptionPane.showMessageDialog(null,"Error en la carga de las imagenes secuenciales");
				}
			}

		});
	}
	
	private void agregarMenuCerrar() {
		itemCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cerrarActionPerformed(evt);
			}
		});
	}
	
	private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}
	
	private void cargarImagen(Imagen imagen){
		this.imagen = imagen;
		ObjProcesamiento.setImagen(imagen);
		contenedorDeImagen.setIcon(new ImageIcon(imagen));
	}
	
	private void agregarMenuCargar(){
		itemCargar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {	
					cargarActionPermorfed(false);
				}catch(Exception e){
					System.out.println("ERROR DE CARGA ARCHIVO: "+ObjProcesamiento.getNombreArchivoImagen());
				}	
			}
		});
	}

	private void cargarActionPermorfed(boolean esSecuencial) {
		cargarImagen(ObjProcesamiento.abrirImagen(esSecuencial));
	}	
}