package org.diverproject.jni.input;

import org.diverproject.util.service.Service;

/**
 * <h1>Despachante para Teclado</h1>
 *
 * <p>Deve ser implementada por qualquer classe que queira despachar eventos de teclado.
 * Possuindo apenas um método que será usado para fazer o despache dos eventos.
 * Todos eventos detectados no sistema serão passados para esse procedimento.</p>
 *
 * <p>Para bom uso desse serviço, deverá ser feito um identificação da ação que foi
 * executada pelo evento e redirecionar cada um dos tipos de ações adequadamente.
 * Além disso deve implementar funções de um serviço como outro qualquer.</p>
 *
 * @author Andrew
 */

public interface KeyboardDispatcher extends Service
{
	/**
	 * Chamado sempre que um evento de teclado for detectado no sistema.
	 * Para que isso ocorra, deverá ser definido no sistema de entrada,
	 * como o responsável por fazer o despache dos eventos de teclado.
	 * @param event referência do evento de teclado detectado no sistema.
	 */

	void dispatch(KeyEvent event);
}
