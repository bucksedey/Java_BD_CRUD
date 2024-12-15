
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Vista extends JFrame
{
	private JTextField tNickname  = new JTextField();
	private JTextField tNombre= new JTextField();
	private JButton    btBuscar  = new JButton("Buscar");
        private JButton    btInsertar  = new JButton("Agregar");
        private JButton    btBorrar  = new JButton("Eliminar");
        private JButton    btCambiar  = new JButton("Cambiar");
        private JButton    btLimpiar = new JButton("Limpiar");
        
        
	private JLabel  imagen    = new JLabel();

	private Properties prop;
        
        private String mensajeError = "";


	public Vista(Properties prop)
	{
                this.prop = prop;
		initComponents();
		this.setTitle("Librerias de Cristal (CRUD del Catálogo)");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLayout(null);
		this.setBounds(10,10,700,300);
	
                // Carga las propiedades desde el archivo
		try
		{
			prop.load(new FileInputStream("config.properties"));
		}
		catch (IOException ex)
		{
			ex.printStackTrace(System.out);
		}
	}

	public final void initComponents()
	{
                       
            JLabel et1        = new JLabel("NICKNAME:");
            JLabel et2        = new JLabel("Nombre:");
            
            // Diseña el menu

            JMenuBar barraMenus = new JMenuBar();
            JMenu menuArchivo 	  = new JMenu("Archivo");
	    JMenuItem opSalir   = new JMenuItem("Salir");
            this.setJMenuBar(barraMenus);

            barraMenus.add(menuArchivo);
            menuArchivo.add(opSalir);

		// Componentes

	    et1.setBounds(10,30,100,25);
	    et2.setBounds(10,70,100,25);
	    imagen.setBounds(530,30,125,170);
	    tNickname.setBounds(80,30,100,25);
	    tNombre.setBounds(80,70,440,25);
            
            // Botones
	    btBuscar.setBounds(440,30,80,25);
            btInsertar.setBounds(80,110,80,25);
            btBorrar.setBounds(200,110,80,25);
            btCambiar.setBounds(320,110,85,25);
            btLimpiar.setBounds(440,110,80,25);
            
            
	    add(et1);
	    add(et2);
	    add(tNickname);
	    add(tNombre);
	    add(btBuscar);
            add(btInsertar);
            add(btBorrar);
            add(btCambiar);
            add(btLimpiar);
	    add(imagen);
            
            opSalir.addActionListener(evt -> gestionaSalir(evt));
            btBuscar.addActionListener(evt -> gestionaBuscar(evt));
            btInsertar.addActionListener(evt -> gestionaInsertar(evt));
            btBorrar.addActionListener(evt -> gestionaBorrar(evt));
            btCambiar.addActionListener(evt -> gestionaCambiar(evt));
            btLimpiar.addActionListener(evt -> gestionaLimpiar(evt));
            
            
            class MyWindowAdapter extends WindowAdapter
            {
                @Override
		public void windowClosing(WindowEvent e)
		{
			exit();
		}
            }
            addWindowListener(new MyWindowAdapter());   

	}

        // Gestión de eventos
        
        public void gestionaSalir(java.awt.event.ActionEvent evt)
        {
            exit();
        }
    
        public void exit()
        {
            int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea salir?","Aviso",JOptionPane.YES_NO_OPTION);
            if(respuesta==JOptionPane.YES_OPTION) System.exit(0);
        }
        
        public void gestionaBuscar(java.awt.event.ActionEvent evt)
        {
            if(tNickname.getText().isBlank())
            {
		JOptionPane.showMessageDialog(this, "Para localizar una contacto se requiere el NICKNAME", "Aviso!", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                Contacto newContact = Contacto.getContactoFromDB(tNickname.getText(),prop); // Método estático para obtener un contacto desde la BD 
		if(newContact != null) // Si hubo éxito
		{
                    tNombre.setText(newContact.getNombre());
                    String nombreArchivoImagen = newContact.getImagen();
                    imagen.setIcon(new ImageIcon(nombreArchivoImagen));
		}
                else JOptionPane.showMessageDialog(this, "La contacto con el NICKNAME indicado no fue localizado", "Aviso!", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
        public void gestionaCambiar(java.awt.event.ActionEvent evt)
        {
            if(tNickname.getText().isBlank())
            {
                JOptionPane.showMessageDialog(this, "Para localizar a la contacto que se va \na actualizar se requiere el NICKNAME", "Aviso!", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                
                if(!invalido()) // Se intenta realizar el UPDATE solo si no hay error de captura
                {
                    Contacto newContact = Contacto.getContactoFromDB(tNickname.getText(),prop); // Método estático para obtener un contacto desde la BD 
                    if(newContact != null)
                    {
                        newContact.setNombre(tNombre.getText()); // Actualiza el titulo del objeto contacto
                        
                        if(newContact.cambiar(prop)) // Si hubo éxito
                            JOptionPane.showMessageDialog(this, "Registro actualizado: " + tNickname.getText(), "Aviso!",JOptionPane.INFORMATION_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(this, "Acción no realizada!!","Aviso!",JOptionPane.ERROR_MESSAGE);
                    }
                    else JOptionPane.showMessageDialog(this, "La contacto con el NICKNAME indicado no fue localizado", "Aviso!", JOptionPane.ERROR_MESSAGE);                    
                } 
                else JOptionPane.showMessageDialog(this, mensajeError, "Aviso!", JOptionPane.ERROR_MESSAGE);                
	    }
                                       
        }
        
        public void gestionaInsertar(java.awt.event.ActionEvent evt)
        {
            if(!invalido()) // Se intenta realizar el INSERT solo si no hay error de captura
            {               
               // Primero investigamos si no hay otro registro con el mismo NICKNAME
                Contacto newContact = Contacto.getContactoFromDB(tNickname.getText(),prop);
               
                if(newContact == null) // Solo si el NICKNAME no está registrado 
                {                        
                    // Adquirimos los datos de la vista              
                    newContact = new Contacto();
                    newContact.setNickname(tNickname.getText());
                    newContact.setNombre(tNombre.getText());
                
                    // Tratamos de ejecutar el alta
                                
                    if(newContact.alta(prop)) // Si la alta fue exitosa
                        JOptionPane.showMessageDialog(this, "Registro agregado: " + tNickname.getText(), "Aviso!",JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(this, "Acción no realizada!!","Aviso!",JOptionPane.ERROR_MESSAGE);                           
                }
                else JOptionPane.showMessageDialog(this, "El NICKNAME ya está registrado", "Aviso!", JOptionPane.ERROR_MESSAGE); 
            } 
            else JOptionPane.showMessageDialog(this, mensajeError, "Aviso!", JOptionPane.ERROR_MESSAGE);          
        }
        
        public void gestionaBorrar(java.awt.event.ActionEvent evt)
        {
            if(tNickname.getText().isBlank())
            {
                JOptionPane.showMessageDialog(this, "Para localizar a la contacto que se va \na eliminar se requiere el NICKNAME", "Aviso!", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                // Solicitamos confirmación     
                int respuesta = JOptionPane.showConfirmDialog(this, "Desea borrar este registro?", "Atención!!!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                if(respuesta==JOptionPane.YES_OPTION) // Si el usuario confirma
                {

                    Contacto newContact = Contacto.getContactoFromDB(tNickname.getText(),prop); // Trata de recuperar el contacto de la BD
                    
                    if(newContact != null) // Si lo encuentra
                    {
                        // Intenta eliminar el registro
                        if(newContact.borrar(prop)) // Si hubo éxito
                        {   
                            JOptionPane.showMessageDialog(this, "Registro eliminado: " + tNickname.getText(), "Aviso!",JOptionPane.WARNING_MESSAGE);
                            limpiarCampos();
                        }    
                        else JOptionPane.showMessageDialog(this, "Acción no realizada!!","Aviso!",JOptionPane.ERROR_MESSAGE);
                    }
                    else JOptionPane.showMessageDialog(this, "El la contacto con el NICKNAME indicado no fue localizado", "Aviso!", JOptionPane.ERROR_MESSAGE);            
		}
            }
            
        }
        
        public void gestionaLimpiar(java.awt.event.ActionEvent evt)
        {
            limpiarCampos();
        }
        
        private void limpiarCampos()
        {
                    tNickname.setText("");
                    tNombre.setText("");
                    imagen.setIcon(null);
        }
        
        // Validación de datos
        private boolean invalido()
        {
            boolean hayError = false;
            mensajeError = "";
            
            if(tNickname.getText().isBlank())
            {
                hayError = true;
                mensajeError = mensajeError.concat("No debe dejar el NICKNAME en blanco\n");
            }
            
            if(tNombre.getText().isBlank())
            {
                        hayError = true;
                        mensajeError = mensajeError.concat("No debe dejar el título en blanco\n");
            }
            
            return hayError;
        }
}