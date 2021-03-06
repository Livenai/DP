package mapa;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import armas.Arma;
import comparadores.OrdenarSalasPorFrecuencia;
import contenedores.Grafo;
import personaje.Personaje;
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
	
	
	//[alto][ancho]   [ID/alto][ID%ancho] so GOOD 
	private  Sala[][] mapita; // tablero con todas las salas
	private Grafo grafo; //grafo asociado al mapa
	private  TheDailyPlanet SalaHombrePuerta; // puntero a la sala de ElHombrePuerta
	private  int ancho;
	private  int alto;
	private int turnoActual;//turno actual de la simulación
	
	public static final int MAXTURNOS = 50; //turnos maximos de simulacion
	public boolean FINSIMULACION = false; //dice si se ha terminado la simulación debidoa que alguien ha cruzado el portal
	private Sala ganadores;//sala donde se almacenara a los personajes ganadores. (1111)
	
	private boolean frecuenciasHechas = false;//sirve para controllar que no se hallen las frecuencias de las salas mas de una vez
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
		turnoActual = 0;
		ganadores = new Sala(1111);
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
		turnoActual = 0;
		ganadores = new Sala(1111);
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
		System.out.println("\nMostrando el mapa:");
		 for (int i = 0; i < ancho; i++) {
		        System.out.print(" _");
		    }
		    System.out.println();
		    
		    for(int j = 0; j < alto; j++) {//alto
		        System.out.print("|");
		        for(int i = 0; i < ancho; i++) {//ancho
		            if(mapita[j][i].getPJDentro().obtenerTam() > 1) {
		                System.out.print(""+mapita[j][i].getPJDentro().obtenerTam());
		            } else if(mapita[j][i].getPJDentro().obtenerTam() == 1) {
		                System.out.print(""+mapita[j][i].getPJDentro().obtenerElemento(0).getID());
		            } else {
		                if(j == alto - 1) {
		                    System.out.print("_");
		                } else
		                	if(mapita[j][i].isS()) {
		                    System.out.print("_");
		                } else {
		                    System.out.print(" ");
		                }
		            }
		            
		            if(i == ancho - 1) {
		                System.out.print("|");
		            } else if(mapita[j][i].isE()) {
		                System.out.print("|");
		            } else {
		                System.out.print(" ");
		            }
		        }
		        
		        System.out.println();
		    }
	}//--------
	
	
	/**
	 * Metodo que registrael mapa en el log en su estado actual
	 * @param mostrarPJ -> Si True muestra los pj en el mapa
	 */
	public void registrarMapa(boolean mostrarPJ) {
		 for (int i = 0; i < ancho; i++) {
		        log.log.write(" _");
		    }
		    log.log.write("\n");
		    
		    for(int j = 0; j < alto; j++) {//alto
		        log.log.write("|");
		        for(int i = 0; i < ancho; i++) {//ancho

		            if(mostrarPJ && mapita[j][i].getPJDentro().obtenerTam() > 1) {
		                log.log.write(""+mapita[j][i].getPJDentro().obtenerTam());
		            } else if(mostrarPJ && mapita[j][i].getPJDentro().obtenerTam() == 1) {
		                log.log.write(""+mapita[j][i].getPJDentro().obtenerElemento(0).getID());
		            } else {
		        	
		                if(j == alto - 1) {
		                    log.log.write("_");
		                } else
		                	if(mapita[j][i].isS()) {
		                    log.log.write("_");
		                } else {
		                    log.log.write(" ");
		                }
		            }
		            
		            if(i == ancho - 1) {
		                log.log.write("|");
		            } else if(mapita[j][i].isE()) {
		                log.log.write("|");
		            } else {
		                log.log.write(" ");
		            }
		        }
		        
		        log.log.write("\n");
		    }
	}
	
	
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
		// Se insertan las armas en las salas mas cpncurrentes del mapa de 5 en 5
		//criterio: Las salas mas frecuentes en todos los caminos desde el origen (0) al destino (TheDailyPlanet)
		//			sin contar estas dos.
		
		//antes de empezar tenemos que establecer los valores de la variable frecuencia de cada sala
		//para ello llamamos al sguiente metodo:
		
		Mapa.obtenerUnico().establecerFrecuenciasDeSalas();
		
		//primero obtenemos las salas mas frecuentes
		LinkedList<Sala> ordenadasPorFrecuencia = getSalasOrdenadasPor(new OrdenarSalasPorFrecuencia());
		System.err.println("orden de salas: -----------------------------");
		for (int i = 0; i < ordenadasPorFrecuencia.size(); i++) {
			System.err.println("[" + ordenadasPorFrecuencia.get(i).getID() + "] frec: " + ordenadasPorFrecuencia.get(i).getFrecuencia() + "   armas: " + ordenadasPorFrecuencia.get(i).getArmasDentro().obtenerTam() );
		}
		System.err.println("orden de salas: -----------------------------");
		
		
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

		
		for (int i = 0; i < 12; i++) {//las 12 primeras
			int candidata = ordenadasPorFrecuencia.removeFirst().getID();
			for (int j=i*5 ; j < (i*5)+5; j++) {
				UNICO.getSalaConID(candidata).insertarArmaEnOrdenDePoder(armasSalas[j]);
			}
		}
	}//-----
	
	

	/**
	 * Metodo que devuelve una LinkedList de Salas con las salas ordenadas segun el Comparator dado
	 * @param ordenarSalasPorFrecuencia -> Comparator de Salas
	 * @return -> LinkedList de salas ordenadas
	 */
	public LinkedList<Sala> getSalasOrdenadasPor(Comparator<Sala> ordenarSalasPorFrecuencia) {
		LinkedList<Sala> ret = new LinkedList<Sala>();
		//primero obtenemos todas las salas en la lista
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				ret.add(UNICO.getSalaConCoor(i, j));
			}
		}
		//ahora ordenamos por el criterio del Comparator
		Collections.sort(ret, new OrdenarSalasPorFrecuencia());

		return ret;
	}
	
	/**
	 * Metodo que calcula todos los caminos desde la sala 0 a la TheDailyPlanet y 
	 * establece lafrecuencia de aparicion de cada sala.
	 * @Recall LLamar a este metodo mas de una vez no surtira ningun efecto.
	 */
	private void establecerFrecuenciasDeSalas() {
		if(!frecuenciasHechas){
			//primero obtenemos los distintos caminos desde el origen al destino
			LinkedList<LinkedList<Integer>> caminos = grafo.obtenerPosiblesCaminos(0,SalaHombrePuerta.getID());
			
			//despues aumentamos la frecuencia de cada sala por su aparicion
			
			for (int i = 0; i < caminos.size(); i++) {
				for (int j = 0; j < caminos.get(i).size(); j++) {//ojo -> no tenemos en cuenta el primer nodo(0) y el ultimo (destino, TheDailyPlanet)
					getSalaConID(caminos.get(i).get(j)).aumentarFrecuencia(1);
				}
			}
			//y nos aseguramos de que esto no se pueda repetir
			frecuenciasHechas = true;
		}
	}
	
	
	public LinkedList<LinkedList<Integer>> getPosiblesCaminos(int origen, int destino){
		LinkedList<LinkedList<Integer>> caminos = grafo.obtenerPosiblesCaminos(origen,destino);
		return caminos;
	}

	/**
	 * Metodo que inicia la simulacion del programa.
	 * 
	 * CUIDADO -> este metodo inicia la simulacion, pero noprepara todo
	 * lo que hay que hacer antes de que esta se inicie. Si la preparacion previa
	 *  no se ha hecho correctamente, este metodo no deberia ser utiizado, pues 
	 *  no se puede asegurar su correcto funcionamiento. 
	 */
	public void iniciarSimulacion() {
		System.out.println("\n------------------------------Iniciando Simulacion------------------------------\n");
		
		/*la simulacion consiste en
		 * 1- recorrer todas las sala
		 * 		a- Para cada sala, procesar el turno de todos los pj
		 * 		b- Si un pj ya ha sido procesado este turno, no vuelve a ser procesado
		 * 2- anotar los cambios que produce lasimulación
		 * 3- repetir hasta el numero maximo de turnos (cte MAXTURNOS)
		 */
		for (int T = 0; !FINSIMULACION && T < MAXTURNOS; T++,turnoActual++) {

			
			
			//procesado del turno:
			System.out.println("____________________________________             \t_______________________________\n"
					+ "____________________________________  Turno:\t" + T + "\t_______________________________\n"
							+ "_______________________________________________________________________________________\n");
			
			for (int i = 0; i < alto; i++) {
				for (int j = 0; j < ancho; j++) {
					Sala candidata = getSalaConCoor(i, j);
					candidata.procesarTurnos();
					
				}
			}

			mostrarMapa();
			System.out.println("_______________________________________________________________________________________");
			
			
			//fase de registro:
			log.log.write("(turn:" + turnoActual + ")\n");
			log.log.write("(map:" + getSalaHombrePuerta().getID() + ")\n");
			getEHP().registrarEstadoEHP();
			registrarMapa(true);
			registrarSalasConArmas();
			registrarPJDeTodasLasSalas();
			
			//si hay alguien en la sala de ganadores se registra
			int numGanadores = ganadores.getPJDentro().obtenerTam();
			if(numGanadores > 0){
				log.log.write("(teseractomembers)\n");
				log.log.write("(owneroftheworld:");
				ganadores.getPJDentro().obtenerPrimero().registrarPJ(false);
				log.log.write(")\n");
				//ahora registramos a los que estan dentro de la sala de ganadores pero llegaron despues
				for (int i = 1; i < numGanadores; i++) {
					ganadores.getPJDentro().obtenerElemento(i).registrarPJ(true);
				}
			}
		}
		
		

		
		

	}


	/**
	 * Registra en el log todos los pj de las salas
	 */
	private void registrarPJDeTodasLasSalas() {
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				getSalaConCoor(i, j).registrarPJDeLaSala();
			}
		}
	}

	/**
	 * Metodo que registra en el log las salas que tengan armas y cuales son.
	 */
	private void registrarSalasConArmas() {		
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				Sala candidata = getSalaConCoor(i, j);
				if(candidata.esSalaConARmas())
				candidata.registrarArmasDeLaSala();
			}
		}
	}

	/**
	 * Metodo que tede vuelve (Y BORRA) el mejor arma de la sala indicada.
	 * @param sala -> sala a buscar(su ID)
	 * @return -> Arma si hay al menos una; null en otro caso
	 */
	public Arma getMejorArmaDeSala(int sala) {
		return UNICO.mapita[sala/UNICO.ancho][sala%UNICO.ancho].obtenerMejorArmaDeLaSala();
	}
	
	
	/**
	 * Inserta el arma dada en la sala con el id dado.
	 * (sigue el orden de inserccion de las salas-> mantener la de mayor poder arriba)
	 * @param salaID -> sala en la que se va a insertar
	 * @param a -> arma a insertar en la sala
	 * @return
	 */
	public void insertarArmaEnOrdenEn(Arma a, int salaID) {
		UNICO.mapita[salaID/UNICO.ancho][salaID%UNICO.ancho].insertarArmaEnOrdenDePoder(a);
		
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
		return UNICO.mapita[ID/UNICO.ancho][ID%UNICO.ancho];
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

	/**
	 * Metodo que devuelve la sala en
	 * la esquina NE (superior derecha)
	 * @return Sala
	 */
	public Sala getEsquinaNorEste() {
		return this.getSalaConCoor(0, this.ancho-1);
	}
	
	/**
	 * Metodo que devuelve la sala en
	 * la esquina NO (superior izquierda)
	 * @return Sala
	 */
	public Sala getEsquinaNorOeste() {
		return this.getSalaConCoor(0, 0);
	}
	
	/**
	 * Metodo que devuelve la sala en
	 * la esquina SE (inferior derecha)
	 * @return Sala
	 */
	public Sala getEsquinaSurEste() {
		return this.getSalaConCoor(this.alto-1, this.ancho-1);
	}
	
	/**
	 * Metodo que devuelve la sala en
	 * la esquina SO (inferior izquierda)
	 * @return Sala
	 */
	public Sala getEsquinaSurOeste() {
		return this.getSalaConCoor(this.alto-1, 0);
	}

	public void setGrafo(Grafo g) {
		grafo = g;
	}

	public boolean hayMuroEntre(int A, int B) {
		return (!grafo.adyacente(A, B));
	}

	public void mostrarPJ() {
		System.out.println("Mostrando Personajes:\n");
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				getSalaConCoor(i, j).mostrarPJ();
			}
		}
		System.out.println("---------------------\n");
		
	}

	public int getTurno() {
		return turnoActual;
	}

	
	/**
	 * Metodo que mueve un pj de la sala origen (salaActual) a la
	 *  destino en función de la dirección.
	 * @param yo -> Personaje
	 * @param salaActual -> ID de la sala origen
	 * @param dir -> Char. Direccion de movimiento (N,S,E,W)
	 */
	public void moverPJconDir(Personaje yo, int salaActual, char dir) {
		//primero sacamos al pj de si sala origen
		Sala origen = getSalaConID(salaActual);
		Personaje pj = origen.SacarPJ(yo);

		//ahora nos insertamos en la sala destino
		Sala destino = null;
		switch(dir){
		case 'N':
			destino = getSalaConID(origen.getID()-ancho);
			break;
		case 'S':
			destino = getSalaConID(origen.getID()+ancho);
			break;
		case 'E':
			destino = getSalaConID(origen.getID()+1);
			break;
		case 'W':
			destino = getSalaConID(origen.getID()-1);
			break;
		}
		if(destino != null){
			destino.insertarPJ(pj);
			yo.setDondeEstoy(destino.getID());
		} else {
			System.err.println("[:(] La sala destino [actual:" + salaActual + " + dir:" + dir + "] vale null");
		}
		
	}

	/**
	 * Metodo que convierte a un pj en ganador. de esta manera, lo sacade 
	 * su sala actual y lo introduce en la sala de ganadores (1111).
	 * @param p -> el pj
	 */
	public void ganar(Personaje p) {
		System.out.println("\n[WIN] ¡Ganador: " + p.getNombre() + "[" + p.getID() + "] !");
		FINSIMULACION = true;
		//sacamos al pj de su sala y lo llevamos a la sala de ganadores
		Sala origen = getSalaConID(p.getDondeEstoy());
		origen.SacarPJ(p);
		p.setDondeEstoy(1111);
		ganadores.insertarPJ(p);		
	}

	/**
	 * Metodo que registra en el log los caminos de los pj siguiendo
	 *  lasiguiente estructura:  
	 *  path:marca del personaje:secuencia de orientaciones
	 */
	public void registrarCaminosDePJ(){
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				getSalaConCoor(i, j).registrarCaminosDePJ();
			}
		}
	}
	
	
	/**
	 * Metodo que inicia el proceso de calculo de rutas de los personajes.
	 */
	public void calcularRutasDePJ(){
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				getSalaConCoor(i, j).calcularRutasDeLosPJ();
			}
		}
	}

	public Grafo getGrafo() {
		return grafo;
	}
	


}
