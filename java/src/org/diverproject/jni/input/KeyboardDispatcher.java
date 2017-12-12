package org.diverproject.jni.input;

import org.diverproject.util.service.Service;

/**
 * <h1>Despachante para Teclado</h1>
 *
 * <p>Deve ser implementada por qualquer classe que queira despachar eventos de teclado.
 * Possuindo apenas um m�todo que ser� usado para fazer o despache dos eventos.
 * Todos eventos detectados no sistema ser�o passados para esse procedimento.</p>
 *
 * <p>Para bom uso desse servi�o, dever� ser feito um identifica��o da a��o que foi
 * executada pelo evento e redirecionar cada um dos tipos de a��es adequadamente.
 * Al�m disso deve implementar fun��es de um servi�o como outro qualquer.</p>
 *
 * @author Andrew
 */

public interface KeyboardDispatcher extends Service
{
	/**
	 * Chamado sempre que um evento de teclado for detectado no sistema.
	 * Para que isso ocorra, dever� ser definido no sistema de entrada,
	 * como o respons�vel por fazer o despache dos eventos de teclado.
	 * @param event refer�ncia do evento de teclado detectado no sistema.
	 */

	void dispatch(KeyEvent event);
}
