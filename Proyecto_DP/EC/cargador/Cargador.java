package cargador;

import java.util.List;

import mapa.Mapa;
import personaje.Villano;

/**
 * Clase creada para ser usada en la utilidad cargador
 * contiene el main del cargador. Se crea una instancia de la clase Estacion, una instancia de la clase Cargador
 * y se procesa el fichero de inicio, es decir, se leen todas las líneas y se van creando todas las instancias de la simulación
 * 
 * @version 6.0 -  17/11/2017 
 * @author Profesores DP
 */
public class Cargador {
	/**  
	número de elementos distintos que tendrá la simulación:
	Mapa, SHPhysical, SHExtraSensorial, SHFlight, Villain
	*/
	static final int NUMELTOSCONF = 5;
	/**  
	atributo para almacenar el mapeo de los distintos elementos
	*/
	static private DatoMapeo [] mapeo;
	
	/**
	 *  constructor parametrizado 
	 *  @param e referencia a la instancia del patrón Singleton
	 */
	public Cargador()  {
		/*
		 * ¿como meter un nuevo tipo de personaje?
		 * en cargador, justo aqui hay que meter un tipo mas del nuevo tipo de pj.
		 * 		//mapeo[5]= new DatoMapeo("SVILLAIN", 4); //el 4 es el numero de 
		 * 												//elementos que se tienen que leer del fichero para este tipo
		 * 
		 * Luego añadimos el nuevo metodo de creacion como en el resto y listo
		 * 
		 * */
		
		mapeo = new DatoMapeo[NUMELTOSCONF];
		mapeo[0]= new DatoMapeo("MAP", 5);
		mapeo[1]= new DatoMapeo("SHPHYSICAL", 4);
		mapeo[2]= new DatoMapeo("SHEXTRASENSORIAL", 4);
		mapeo[3]= new DatoMapeo("SHFLIGHT", 4);
		mapeo[4]= new DatoMapeo("VILLAIN", 4);
		
	}
	
	/**
	 *  busca en mapeo el elemento leído del fichero inicio.txt y devuelve la posición en la que está 
	 *  @param elto elemento a buscar en el array
	 *  @return res posición en mapeo de dicho elemento
	 */
	private int queElemento(String elto)  {
	    int res=-1;
	    boolean enc=false;

	    for (int i=0; (i < NUMELTOSCONF && !enc); i++)  {
	        if (mapeo[i].getNombre().equals(elto))      {
	            res=i;
	            enc=true;
	        }
	    }
	    return res;
	}
	
	/**
	 *  método que crea las distintas instancias de la simulación 
	 *  @param elto nombre de la instancia que se pretende crear
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo de la instancia
	 */
	public void crear(String elto, int numCampos, List<String> vCampos)	{
	    //Si existe elemento y el número de campos es correcto, procesarlo... si no, error
	    int numElto = queElemento(elto);

	    //Comprobación de datos básicos correctos
	    if ((numElto!=-1) && (mapeo[numElto].getCampos() == numCampos))   {
	        //procesar
	        switch(numElto){
	        case 0:	   
	            crearMap(numCampos,vCampos); // TODO hay que crear estos metodos que sonlos que crean
	            break;
	        case 1:
	            crearSHPhysical(numCampos,vCampos);
	            break;
	        case 2:
	            crearSHExtraSensorial(numCampos,vCampos);
	            break;
	        case 3:
	            crearSHFlight(numCampos,vCampos);
	            break;
	        case 4:
	            crearVillain(numCampos,vCampos);
	            break;
	     	}
	    }
	    else
	        System.out.println("ERROR Cargador::crear: Datos de configuración incorrectos... " + elto + "," + numCampos+"\n");
	}

	/**
	 *  método que crea una instancia de la clase Planta
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearMap(int numCampos, List<String> vCampos){
	    System.out.println("Creado Map: [" + vCampos.get(1) + "," + vCampos.get(2) + "] con cte [" + vCampos.get(4) + "]\n");
	    //TODO inicializar mapa y el resto de metodos
	    Mapa.obtenerUnico(Integer.parseInt(vCampos.get(1)),
	    				Integer.parseInt(vCampos.get(2)),
	    				Integer.parseInt(vCampos.get(3)),
	    				Integer.parseInt(vCampos.get(4)));
	}

	/**
	 *  método que crea una instancia de la clase SHPhysical
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearSHPhysical(int numCampos, List<String> vCampos){
	    System.out.println("Creado SHPhysical: " + vCampos.get(1) + "\n");
	    //Registrar SHPhysical en el mapa
	}

	/**
	 *  método que crea una instancia de la clase SHExtraSensorial
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearSHExtraSensorial(int numCampos, List<String> vCampos){
	    System.out.println("Creado SHExtraSensorial: " + vCampos.get(1) + "\n");
	    //Registrar SHExtraSensorial en el mapa
	}	

	/**
	 *  método que crea una instancia de la clase SHFlight
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearSHFlight(int numCampos, List<String> vCampos){
	    System.out.println("Creado SHFlight: " + vCampos.get(1) + "\n");
	    //Registrar SHFlight en el mapa
	}	

	/**
	 *  método que crea una instancia de la clase Villain
	 *  @param numCampos número de atributos que tendrá la instancia
	 *  @param vCampos array que contiene los valores de cada atributo
	 */
	private void crearVillain(int numCampos, List<String> vCampos){
	    System.out.println("Creado Villano: " + vCampos.get(1) + "[" + vCampos.get(2) + "]. turno: " + vCampos.get(3)+  "\n");
	    //Registrar Villain en el mapa
	    Villano v = new Villano(vCampos.get(1),
	    						vCampos.get(2).charAt(0),
	    						Mapa.getEsquinaNorEste()),
	    						Integer.parseInt(vCampos.get(3)));
	    Mapa.obtenerUnico().insertarPJ(getEsquinaNorEste, pj);//TODO terminar esto
	}

}
