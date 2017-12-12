
package org.diverproject.jni.input;

/**
 * <h1>Leitura de Tecla</h1>
 *
 * <p>Objetos com essa interface podem ser adicionados ao despachador de teclado.
 * Quando o teclado receber eventos de teclas irá repassar esse através dos métodos
 * implementados por essa interface, cada tipo de evento em um único método.</p>
 *
 * <p>Uma observação que deverá ser feita em relação aos eventos desencadeados.
 * Sempre que um evento de pressionar for detectado um de digitar também será.
 * Apenas os eventos de digitar terão um caracter vinculado ao mesmo.</p>
 *
 * @author Andrew
 */

public interface KeyListener
{
	/**
	 * Chamado sempre que uma tecla estiver sendo digitada, em quanto pressionado.
	 * @param event objeto contendo as informações do evento detectado no teclado.
	 */

	void keyTyped(KeyEvent event);

	/**
	 * Chamado sempre que uma tecla estiver sendo pressionado, em quanto pressionado.
	 * @param event objeto contendo as informações do evento detectado no teclado.
	 */

	void keyPressed(KeyEvent event);

	/**
	 * Chamado sempre que uma tecla parar de ser pressionado, apenas uma vez.
	 * @param event objeto contendo as informações do evento detectado no teclado.
	 */

	void keyReleased(KeyEvent event);
}
