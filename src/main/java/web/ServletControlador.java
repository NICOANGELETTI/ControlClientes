package web;

import datos.ClienteDaoJDBC;
import dominio.Cliente;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
      
         String accion = request.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "editar":
                    this.EditarCliente(request,response);
                    break;
                case "eliminar":
                    this.eliminarCliente(request, response);
                    break;
                default:
                    this.AccionDefault(request, response);

            }

        } else {
            this.AccionDefault(request, response);
        }
    }
    
    
    private void EditarCliente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
    
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        Cliente cliente = new ClienteDaoJDBC().encontrar(new Cliente(idCliente));
        request.setAttribute("cliente", cliente);
        String jspEdit = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEdit).forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "insertar":
                    this.InsertarCliente(request, response);
                    break;

                case "modificar":
                    this.modificarCliente(request, response);
                    break;
           
                default:
                    this.AccionDefault(request, response);

            }

        } else {
            this.AccionDefault(request, response);
        }
    }

    protected void AccionDefault(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        List<Cliente> clientes = new ClienteDaoJDBC().listar();
        System.out.println("clientes = " + clientes);
        HttpSession sesion = request.getSession();
        sesion.setAttribute("clientes", clientes);
        //Total de clientes
        sesion.setAttribute("totalClientes", clientes.size());
        sesion.setAttribute("saldoTotal", this.ObtenerSaldo(clientes));
        response.sendRedirect("clientes.jsp");

    }
    protected void InsertarCliente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        //Traemos a traves de getParameter todos los parametros cargados en el formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
       
        //Definimos saldo de tipo double con el valor de 0
        double saldo = 0;
        //Traemos el saldo en formato String
        String saldoString = request.getParameter("saldo");
        //Hacemos la condicion por si el saldo que traemos es nulo
        if(saldoString !=null && !"".equals(saldoString)){
            //Convertimos el saldoString en un tipo Double guardandolo en la variable saldo
            saldo = Double.parseDouble(saldoString);
                  
            
        }
        
        
        
        
        
        //Creamos la variable cliente de tipo Cliente con sus metodos
        Cliente cliente = new Cliente(nombre,apellido,email,telefono,saldo);
        //Creamos variable registrosModifados de tipo ClienteDao donde inserte el cliente creado
        int registrosModificados =new ClienteDaoJDBC().insertar(cliente);
        
        //Por ultimo actualizamos
        this.AccionDefault(request, response);
        

    }
         protected void modificarCliente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        //Traemos a traves de getParameter todos los parametros cargados en el formulario
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
       
        //Definimos saldo de tipo double con el valor de 0
        double saldo = 0;
        //Traemos el saldo en formato String
        String saldoString = request.getParameter("saldo");
        //Hacemos la condicion por si el saldo que traemos es nulo
        if(saldoString !=null && !"".equals(saldoString)){
            //Convertimos el saldoString en un tipo Double guardandolo en la variable saldo
            saldo = Double.parseDouble(saldoString);
                  
            
        }
        
        
        
          //Creamos la variable cliente de tipo Cliente con sus metodos
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        //Creamos variable registrosModifados de tipo ClienteDao donde inserte el cliente creado
        int registrosModificados = new ClienteDaoJDBC().actualizar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        //Por ultimo actualizamos
        this.AccionDefault(request, response);
        
    }
         
         
          protected void eliminarCliente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        //Traemos a traves de getParameter todos los parametros cargados en el formulario
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        //Creamos la variable cliente de tipo Cliente con sus metodos
        Cliente cliente = new Cliente(idCliente);
        
       //Eliminamos

        int registrosModificados = new ClienteDaoJDBC().eliminar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        //Por ultimo actualizamos
        this.AccionDefault(request, response);
        
    }
    
     private double ObtenerSaldo(List<Cliente> clientes){
            double saldoTotal= 0;
            for(Cliente cliente:clientes){
                saldoTotal+=cliente.getSaldo();
            }
            return saldoTotal;
        }
}
