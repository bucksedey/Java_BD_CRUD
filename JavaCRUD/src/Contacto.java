/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Marco Flores
 */
import java.sql.*;
import java.util.*;

public class Contacto {


	private String nickname;
	private String nombre;
	private String email;
	private String imagen;
	private String celular;

	public Contacto()
	{}

	public Contacto(String nickname, String nombre, String email, String imagen, String celular)
	{
		this.nickname = nickname;
		this.nombre = nombre;
		this.email = email;
		this.imagen = imagen;
		this.celular = celular;

	}

	// Metodos get

	public String getNickname()
	{
		return nickname;
	}

	public String getNombre()
	{
		return nombre;
	}

	public String getEmail()
	{
		return email;
	}

	public String getImagen()
	{
		return "imagenes/" + imagen;
	}

	public  String getCelular()
	{
		return celular;
	}

	// Métodos set

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setImagen(String imagen)
	{
		this.imagen = imagen;
	}

	public void setCelular(String celular)
	{
		this.celular = celular;
	}


	public void muestraDatos()
	{
		System.out.println("NICKNAME  :" + nickname);
		System.out.println("Nombre:" + nombre);
		System.out.println("Email :" + email);
		System.out.println("Celular:" + celular);
	}


	public static Contacto getContactoFromDB(String nicknameConsulta, Properties prop)
	{
            Contacto contacto = new Contacto(); // Nuevo contacto en blanco

            try
	    {

                String driver = prop.getProperty("dbdriver");
                String host   = prop.getProperty("dbhost");
                String user   = prop.getProperty("dbuser");
                String password = prop.getProperty("dbpassword");
                String name     = prop.getProperty("dbname");
                String url = host + name  + "?user=" + user + "&password=" + "buckgresql";
                System.out.println("Conexion a la BD: " + url);


                Class.forName(driver);     // Carga el driver


                Connection con = DriverManager.getConnection(url); // Crea una conexion a la BD

                PreparedStatement ps = con.prepareStatement("SELECT * FROM CONTACTOS WHERE NICKNAME = ?");
                ps.setString(1,nicknameConsulta);
                System.out.println(ps.toString());
                ps.executeQuery();
                ResultSet rs = ps.getResultSet();

                if(rs!=null && rs.next())
                {
                    String nickname   = rs.getString("nickname");
                    String nombre = rs.getString("nombre");
                    String email  = rs.getString("email");
                    String imagen = rs.getString("imagen");
                    String celular = rs.getString("celular");

                    contacto.setNickname(nickname);
                    contacto.setNombre(nombre);
                    contacto.setEmail(email);
                    contacto.setImagen(imagen);
                    contacto.setCelular(celular);
                    con.close();
                    return contacto;
                }

	    }
            catch (ClassNotFoundException | SQLException ex)
	    {
	    	ex.printStackTrace(System.out);
	    }     
	    
            return null;
	}
        
        public boolean cambiar(Properties prop)
	{
            boolean exito = false;
            
            try
	    {

                String driver = prop.getProperty("dbdriver");
                String host   = prop.getProperty("dbhost");
                String user   = prop.getProperty("dbuser");
                String password = prop.getProperty("dbpassword");
                String name     = prop.getProperty("dbname");
                String url = host + name  + "?user=" + user + "&password=" + password;
                System.out.println("Conexion a la BD: " + url);


                Class.forName(driver);     // Carga el driver


                Connection con = DriverManager.getConnection(url); // Crea una conexion a la BD

                PreparedStatement ps = con.prepareStatement("UPDATE CONTACTOS SET NOMBRE = ? WHERE NICKNAME = ?");
                
                ps.setString(1, this.nombre); // El nombre que llega de la Vista
                ps.setString(2, this.nickname);
                System.out.println(ps.toString());
                exito = ps.executeUpdate() > 0;
                con.close();
    
	    }
            
            catch (ClassNotFoundException | SQLException ex)
	    {
	    	ex.printStackTrace(System.out);
	    }            
	    return exito;
	}

        public boolean borrar(Properties prop)
	{
            boolean exito = false;
            
            try
	    {

                String driver = prop.getProperty("dbdriver");
                String host   = prop.getProperty("dbhost");
                String user   = prop.getProperty("dbuser");
                String password = prop.getProperty("dbpassword");
                String name     = prop.getProperty("dbname");
                String url = host + name  + "?user=" + user + "&password=" + password;
                System.out.println("Conexion a la BD: " + url);


                Class.forName(driver);     // Carga el driver


                Connection con = DriverManager.getConnection(url); // Crea una conexion a la BD

                PreparedStatement ps = con.prepareStatement("DELETE FROM CONTACTOS WHERE NICKNAME = ?");
                ps.setString(1, this.nickname);
                System.out.println(ps.toString());
                exito = ps.executeUpdate() > 0;
                con.close();
               
	    }
            catch (ClassNotFoundException | SQLException ex)
	    {
	    	ex.printStackTrace(System.out);
	    }     
	    return exito;
	}

        public boolean alta(Properties prop)
	{
            boolean exito = false;
            
            try
	    {

                String driver = prop.getProperty("dbdriver");
                String host   = prop.getProperty("dbhost");
                String user   = prop.getProperty("dbuser");
                String password = prop.getProperty("dbpassword");
                String name     = prop.getProperty("dbname");
                String url = host + name  + "?user=" + user + "&password=" + password;
                System.out.println("Conexion a la BD: " + url);


                Class.forName(driver);     // Carga el driver


                Connection con = DriverManager.getConnection(url); // Crea una conexion a la BD

                PreparedStatement ps = con.prepareStatement("INSERT INTO CONTACTOS (NICKNAME, NOMBRE) VALUES (?,?)");
                ps.setString(1, this.nickname); 
                ps.setString(2, this.nombre); 
                System.out.println(ps.toString());
                exito = ps.executeUpdate() > 0;
                con.close();
               
	    }
            catch (ClassNotFoundException | SQLException ex)
	    {
	    	ex.printStackTrace(System.out);
	    }     
	    return exito;
	}     
}