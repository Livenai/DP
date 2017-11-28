package personaje;

import java.util.LinkedList;

import armas.Arma;
import contenedores.Arbol;
import mapa.Mapa;
import salas.ElHombrePuerta;
import salas.Sala;

/**
 * Clase que representa el tipo de personaje
 * SuperHeroe
 * @author CARLOS MU�OZ ZAPATA
 * GIIIC
 *
 */
public abstract class SuperHeroe extends Personaje {
	
	private Arbol<Arma> ArmasDelPJ;


	
	/**
	 * Ctor parametrizado (super())
	 * @param nombre
	 * @param id
	 */
	public SuperHeroe(String nombre,char id,int dondeEstoy,int turno) {
		super(nombre,id, dondeEstoy, turno);		
		ArmasDelPJ = new Arbol<Arma>();
		System.out.println("[!] SuperHeroe " + nombre + " [" + id + "] creado.");

	}
	
	/**
	 * getter
	 * @return
	 */
	public Arbol<Arma> getArmasDelPJ() {
		return ArmasDelPJ;
	}

	@Override
	public void interactuarSala() {
		// Superh�roes. Si no tenia previamente este arma, ser� almacenada en su contenedor de armas 
		//(que debe permitir b�squedas de la forma m�s eficiente posible), mientras que si ya la ten�a, se le sumar� 
		//al poder del arma que ten�a el poder del arma que ha recogido. 

		//obtenemos el arma mas potente de la sala
		
		Arma sacada = Mapa.obtenerUnico().getMejorArmaDeSala(getDondeEstoy());
		
		
		//nos la quedamos, si existe claro :3
		
		if(sacada != null){
			if(ArmasDelPJ.pertenece(sacada)){
				//si ya tenemos un arma igual, �JUNTAMOS SU PODER! pero desenvainamos primero la nuestra ~
				Arma laMia = ArmasDelPJ.obtenerBorrando(sacada);
				//juntamos su poder y nos la guardamos
				ArmasDelPJ.insertar(new Arma(laMia.getNombre(),(laMia.getPoder()+sacada.getPoder()))); 
			} else {
				//si no poseiamos ya ese arma, pues nos la quedamos ~
				ArmasDelPJ.insertar(sacada);
			}
		}
		
	}

	@Override
	public void interactuarPuerta() {
		//este interactuar puerta es comun para todos los tipos de SH
		//(excepto si alguno redefine el metodo)

		//antes de nada... ¿estamos en el TheDailyPlanet?
		if(getDondeEstoy()==Mapa.obtenerUnico().getSalaHombrePuerta().getID()){
			//primero obtenemos el arma mas potente de nuestro arsenal
			Arma miCandidata = obtenerArmaMasPotente(true);
			//ahora ElHombrePuerta saca su arma del mismo tipo (si la tiene)
			ElHombrePuerta hp = Mapa.obtenerUnico().getEHP();
			Arma candidataDeEHP = hp.obtenerBorrando(miCandidata);
			
			
	
			
			//ahora... �SE ALZA LA BATALLA! �quien gana? 
			//si el hombre puerta no tiene un arma equiparable... pues nada, no hay batalla :/
			if(candidataDeEHP != null){ 
				if(miCandidata.getPoder() > candidataDeEHP.getPoder()){ // si gana el SuperHeroe
					//pues el super heroe ha ganado y ElHombrePuerta se queda sin arma
				} else {
					//pues ElHombrePuerta ha ganado asique conserva su arma.
					hp.insertarArma(candidataDeEHP);
				}
			}
			//el SuperHeroe siempre pierde su arma en la batalla (o el intento de...) :(
			
			//por ultimo, comprobamos el estado de ElHombrePuerta para que se
			//anoten los cambios producidos en la batalla
			hp.comprobarEstado();
			
			System.out.println("Batalla: " + getNombre() + "["+ getID() +"] contra ElHombrePuerta." + "\nArmas en la batalla: " +
					miCandidata.getNombre() + "[" + miCandidata.getPoder() + "]" + "  vs  " 
					+ candidataDeEHP.getNombre() + "[" + candidataDeEHP.getPoder() + "]" 
					+ "\nResultado del portal -> " + hp.isEstado());
			System.out.println("set de armas de ElHombrePuerta despues de la batalla: ");
			hp.mostrarSetDeArmasActual();System.out.println("\n");
		}
		
	}
	
	
	/**
	 * devuelve el arma mas potente del 
	 * arsenal del SuperHeroe y la borra a gusto.
	 * @param borrar -> true: borra el elemento de la lista;
	 * 					 False: lo mantiene (solo lo devuelve)
	 * @return el arma mas potente
	 */
	public Arma obtenerArmaMasPotente(boolean borrar){
		Arma ret = new Arma("a pu�etazo limpio",-1);
		//primaro obtenemos las armas que que tenemos disponibles
		LinkedList<Arma> ll = ArmasDelPJ.inOrden();
		
		//ahora obtenemos el arma mas potente
		for (int i = 0; i < ll.size(); i++) {
			if(ret.getPoder() < ll.get(i).getPoder()){
				ret = ll.get(i);
			}
		}
		//ahora ret posee el arma mas potente
		
		
		if(borrar){
			ArmasDelPJ.borrar(ret);
		}
		
		
		return ret;
	}

	/**
	 * metodo que inserta un arma en el arbol de armas del SuperHeroe
	 * @param arma
	 */
	public void insertarArma(Arma arma) {
		ArmasDelPJ.insertar(arma);
	}


	@Override
	public void interactuarConOtrosPJ(){
		//Capturará al primer Villano que se encuentre en la sala sólo 
		//si este personaje posee un arma igual a la que posee el villano y tiene mayor poder.
		Mapa uni = Mapa.obtenerUnico();
		Sala salaActual = uni.getSalaConID(this.getID());
		
		//1º) bucamos al primer villano (si no hay ninguno no se hace nada)
		Villano elPrimero = salaActual.sacarPrimerVillano(false);
		if(elPrimero != null){//si existe
			Arma laDelVillano = elPrimero.getArma();
			Arma laNuestra = this.getArmaIgualA(laDelVillano);
			//comprobamos
			if(laNuestra.getPoder()>laDelVillano.getPoder()){
				//ganamos, asique el villano es derrotado
				System.out.println("[X] el Villano " + elPrimero.toString() + " ha sido derrotado por " + this.toString());
				//lo sacamos de la sala
				salaActual.sacarPrimerVillano(true);
			} else {
				//perdemos, asique todo queda como estaba
			}
			//recuperamos nuestro arma
			ArmasDelPJ.insertar(laNuestra);
			
			
		}
	}

	
	/**
	 * Metodo que devuelve el arma que posea el SH similar (mismo nombre)
	 * a la insertada. Si no tiene dcho arma, devuelve null
	 * @param cand -> arma similar
	 * @return arma si la tiene, null en otro caso
	 */
	private Arma getArmaIgualA(Arma cand) {
		return ArmasDelPJ.obtenerBorrando(cand);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();
	
	

	
}
