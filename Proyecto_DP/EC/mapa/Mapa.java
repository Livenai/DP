package mapa;

import java.io.FileNotFoundException;
import java.io.IOException;

import armas.Arma;
import cargador.Cargador;
import cargador.FicheroCarga;
import personaje.Personaje;
import personaje.SuperHeroe;
import personaje.Villano;
import salas.ElHombrePuerta;
import salas.Sala;
import salas.TheDailyPlanet;

/**
 * clase mapa, la cual es Unica y accesible
 * desde cualquier punto
 * @author CARLOS MU�OZ ZAPATA
 * GIIIC
 */
public class Mapa {
	
	// Atributos
	//elemento unico que representa el mapa unico del juego
	private static Mapa UNICO;
	
	//[alto][ancho]   [ID/ancho][ID%ancho] so GOOD
	private  Sala[][] mapita; // tablero con todas las salas
	private  TheDailyPlanet SalaHombrePuerta; // puntero a la sala de ElHombrePuerta
	private  int ancho;
	private  int alto;
	//---------

	/**
	 * Ctor generico.
	 * crea el mapa con los siguientes atributos estandarizados
	 * mapa [6][6]
	 * SalaHombrePuerta(DailyPlanet) -> 35 (en la ultima)
	 * constante de apertura -> 4
	 */
	private Mapa() {
		mapita = new Sala[6][6];
		alto = mapita.length; 
		ancho = mapita[0].length;
		for (int i = 0; i < alto; i++) {//alto
			for (int j = 0; j < ancho; j++) {//ancho
				mapita[i][j] = new Sala((i*ancho)+j);
			}
		}
		//creamos la sala especial y la metemos en la posicion dicha
		SalaHombrePuerta = new TheDailyPlanet(35, 4);
		mapita[alto-1][ancho-1] = SalaHombrePuerta;
		System.out.println("[!] Mapa creado");
	}
	
	/**
	 * Ctor. parametrizado
	 * @param alto
	 * @param ancho
	 * @param TheDailyPlanet
	 */
	private Mapa(int alto_, int ancho_, int TheDailyPlanet,int cteApertura) {
		mapita = new Sala[alto_][ancho_];
		alto = mapita.length;
		ancho = mapita[0].length;
		for (int i = 0; i < alto; i++) {//alto
			for (int j = 0; j < ancho; j++) {//ancho
				mapita[i][j] = new Sala((i*ancho)+j);
			}
		}
		//creamos la sala especial y la metemos en la posicion dicha
		SalaHombrePuerta = new TheDailyPlanet(TheDailyPlanet, cteApertura);
		mapita[TheDailyPlanet/ancho][ TheDailyPlanet%ancho] = SalaHombrePuerta;
		System.out.println("[!] Mapa creado con medidas ANCHO: " + ancho + " ;ALTO: " + alto + " (" + ancho*alto + " salas)");
	}
	
	/**
	 * metodo que devuelve la instancia unica del mapa
	 * si este no ha sido creado, lo crea llamando al ctor default:
	 * h -> 6
	 * a -> 6
	 * hp -> 35
	 * cte -> 4
	 * @return
	 */
	public static Mapa obtenerUnico() {
        if (UNICO == null){
        	System.out.println("singeltone sin param here");
            UNICO = new Mapa();
        }
		return UNICO;
	}
	
	
	/**
	 * Metodo que devuelve la instancia única del mapa
	 * Si este no esta creado, lo crea con los parametros pasados.
	 * @param h -> ancho
	 * @param a -> alto
	 * @param hp -> sala donde se hubicara ElHombrePuerta
	 * @param cte -> cte de apertura del portal
	 * @return
	 */
	public static Mapa obtenerUnico(int h, int a, int hp, int cte) {
        if (UNICO == null){
        	System.out.println("singeltone CON param here");
            UNICO = new Mapa(h,a,hp,cte);
        }
		return UNICO;
	}

	
	
	public Sala[][] getMapita() {
		return mapita;
	}

	public TheDailyPlanet getSalaHombrePuerta() {
		return SalaHombrePuerta;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}
	
	/**
	 * Muestra el mapa por pantalla segun 
	 * el formato dado
	 */
	public void mostrarMapa(){
		System.out.println("Mostrando el mapa:");
		 for (int i = 0; i < ancho; i++) {
		        System.out.print(" _");
		    }
		    System.out.println();
		    
		    for(int j = 0; j < alto; j++) {
		        System.out.print("|");
		        for(int i = 0; i < ancho; i++) {
		            if(mapita[i][j].getPJDentro().obtenerTam() > 1) {
		                System.out.print(""+mapita[i][j].getPJDentro().obtenerTam());
		            } else if(mapita[i][j].getPJDentro().obtenerTam() == 1) {
		                System.out.print(""+mapita[i][j].getPJDentro().obtenerElemento(0).getID());
		            } else {
		                if(j == alto - 1) {
		                    System.out.print("_");
		                } else
		                	if(/*hayMuroS(i, j)*/false) {
		                    System.out.print("_");
		                } else {
		                    System.out.print(" ");
		                }
		            }
		            
		            if(i == ancho - 1) {
		                System.out.print("|");
		            } else if(/*hayMuroD(i, j)*/false) {
		                System.out.print("|");
		            } else {
		                System.out.print(" ");
		            }
		        }
		        
		        System.out.println();
		    }
	}//--------
	
	
	
    /**
     * Muestra las salas que tienen armas y cuales son estas
     */
    public void mostrarSalasConArmas(){
    	System.out.println("Mostrando Salas con Armas:");
    	for (int i = 0; i < ancho*alto; i++) {
			if(!mapita[i/ancho][i%ancho].getArmasDentro().estaVacia()){
				mapita[i/ancho][i%ancho].mostrarArmas();
			}
		}
    }

    /**
     * metodo que inserta un pj en la sala con dicho id
     * si ID no esta en el rango 0-alto*ancho-1; no lo inserta y avisa
     * @param id
     * @param pj
     */
	public void insertarPJ(int id, Personaje pj) {
		if(0 <= id && id < (alto*ancho) ) {
			mapita[id/ancho][id%ancho].insertarPJ(pj);	
		} else {
			System.err.println("Se ha intentado insertar el pj " + pj.getNombre() + ", " + pj.getID() + " en la sala " + id + "\n rango (0 <= id <= " + (ancho*alto) +")");
		}
	}
	
	


//fin del MAIN-----------------------------------------------------------------------------------------------------------------


	// metodos auxiliares de la clase:
	


	/**
	 * Metodo que genera e inserta las llaves en las salas
	 */
	public void insertarArmas() {
		// Al principio se generan estas armas y se insertan en las salas:
		
		int[] idSalasConArmas = {1, 2, 8, 14, 15, 21, 27, 35, 28, 29, 33, 34};
		
        Arma [] armasSalas = {new Arma("Mjolnir",29), new Arma("Anillo",1), new Arma("Garra",27), 
                new Arma("Armadura",3), new Arma("Red",25), new Arma("Escudo",5), 
                new Arma("Lucille",23), new Arma("Lawgiver",7), new Arma("GuanteInfinito",21), 
                new Arma("LazoVerdad",9), new Arma("CadenaFuego",19), new Arma("Capa",11), 
                new Arma("Flecha",17), new Arma("Tridente",13), new  Arma("Antorcha",15), 
                new Arma("Baston",28), new Arma("Latigo",2), new  Arma("MazaOro",26), 
                new Arma("CampoMagnetico",4), new Arma("Tentaculo",24), 
                new Arma("CampoEnergia",6), new Arma("Cetro",22), new Arma("RayoEnergia",8), 
                new Arma("Laser",20), new Arma("Bola",10), new Arma("Espada",18), 
                new Arma("Sable",12),  new Arma("Acido",16), new Arma("Gema",14), 
                new Arma("Nullifier",23), new Arma("Mjolnir",1), new Arma("Anillo",29), 
                new Arma("Garra",3), new Arma("Armadura",27),  new Arma("Red",5), 
                new Arma("Escudo",25), new Arma("Lucille",7), new  Arma("Lawgiver",23), 
                new Arma("GuanteInfinito",9), new Arma("LazoVerdad",21), 
                new Arma("CadenaFuego",11), new Arma("Capa",19), new Arma("Flecha",13), 
                new Arma("Tridente",17), new Arma("Antorcha",28), new Arma("Baston",15), 
                new Arma("Latigo",26), new Arma("MazaOro",2), new Arma("CampoMagnetico",24), 
                new Arma("Tentaculo",4), new Arma("CampoEnergia",22), new Arma("Cetro",6), 
                new Arma("RayoEnergia",20), new Arma("Laser",8), new Arma("Bola",18), 
                new Arma("Espada",10), new Arma("Sable",16), new Arma("Acido",12), 
                new Arma("Gema",1), new Arma("Nullifier",3)};

		
		for (int i = 0; i < idSalasConArmas.length; i++) {
			int idSala = idSalasConArmas[i];
			int H = UNICO.ancho;
			for (int j=i*5 ; j < (i*5)+5; j++) {
				UNICO.mapita[idSala/H][idSala%H].insertarArmaEnOrdenDePoder(armasSalas[j]);
			}
		}
	}//-----
	
	
	/**
	 * Metodo que inicia la simulacion del programa.
	 * 
	 * CUIDADO -> este metodo inicia la simulacion, pero noprepara todo
	 * lo que hay que hacer antes de que esta se inicie. Si la preparacion previa
	 *  no se ha hecho correctamente, este metodo no deberia ser utiizado, pues 
	 *  no se puede asegurar su correcto funcionamiento. 
	 */
	public void iniciarSimulacion() {
		System.out.println("\n----Iniciando Simulacion----\n");
		System.out.println("esta simulacion es de prueba y solo realiza una interaccion \n"
				+ "de lo que seria un turno con todos los pj en TheDailyPlanet \n\n");
		
		
		for (int i = 0; i < UNICO.SalaHombrePuerta.getPJDentro().obtenerTam(); i++) {
			UNICO.SalaHombrePuerta.getPJDentro().obtenerElemento(i).interactuarPuerta();
		}
	}

	/**
	 * Metodo que tede vuelve (Y BORRA) el mejor arma de la sala indicada.
	 * @param sala -> sala a buscar(su ID)
	 * @return -> Arma si hay al menos una; null en otro caso
	 */
	public Arma getMejorArmaDeSala(int sala) {
		return UNICO.mapita[sala%UNICO.ancho][sala/UNICO.alto].obtenerMejorArmaDeLaSala();
	}
	
	
	/**
	 * Inserta el arma dada en la sala con el id dado.
	 * (sigue el orden de inserccion de las salas-> mantener la de mayor poder arriba)
	 * @param salaID -> sala en la que se va a insertar
	 * @param a -> arma a insertar en la sala
	 * @return
	 */
	public void insertarArmaEnOrdenEn(Arma a, int salaID) {
		UNICO.mapita[salaID%UNICO.ancho][salaID/UNICO.alto].insertarArmaEnOrdenDePoder(a);;
	}

	
	/**
	 * Metodo que devuelve elmejor arma de ElHombrePuerta
	 * @return Arma si le queda alguna; null en otro caso
	 */
	public ElHombrePuerta getEHP() {
		
		return UNICO.getSalaHombrePuerta().getHp();
	}

	
	/**
	 * Metodo que devuelve la sala con el ID dado.
	 * @param ID -> id de la sala
	 * @return Sala
	 */
	public Sala getSalaConID(int ID){
		return UNICO.mapita[ID%UNICO.ancho][ID/UNICO.alto];
	}
	
	
	/**
	 * Metodo que devuelve la sala en las coordenadas dadas.
	 * @param a -> Alto
	 * @param h -> ancHo
	 * @return  UNICO.mapita[a][h]
	 */
	public Sala getSalaConCoor(int a, int h){
		return UNICO.mapita[a][h];
	}
}
