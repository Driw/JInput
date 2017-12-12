
package org.diverproject.jni.input;

import org.diverproject.util.collection.Queue;
import org.diverproject.util.collection.abstraction.DynamicQueue;

/**
 * Apanhador para Teclado
 *
 * Essa classe irá receber diretamente os eventos de teclado detectados.
 * Sempre que um novo evento for recebido irá colocá-lo na fila de espera.
 * Uma outra Thread irá garantir que esses eventos sejam despachados.
 *
 * @see Queue
 * @see KeyboardProcessor
 *
 * @author Andrew
 */

class KeyboardPicker
{
	/**
	 * Fila contendo todos os eventos ainda não despachados.
	 */
	Queue<KeyEvent> buffer;

	/**
	 * Processador de teclado que irá despachar a fila de eventos.
	 */
	private KeyboardProcessor processor;

	/**
	 * Constrói um novo apanhador para teclado, deve iniciar a fila de eventos,
	 * como também instanciar uma nova thread para fazer o despache dessa fila.
	 */

	public KeyboardPicker()
	{
		 buffer = new DynamicQueue<KeyEvent>();
		 processor = new KeyboardProcessor(this);
		 processor.start();
	}

	/**
	 * Chamado apenas pela DLL e deve inserir um novo evento a fila de eventos.
	 * @param event referência do evento detectado pelo teclado no sistema.
	 */

	private void receiveKey(KeyEvent event)
	{
		synchronized (buffer)
		{
			buffer.offer(event);
		}
	}

	/**
	 * Deve ser chamado para fazer um novo registro desse objeto na DLL.
	 * Ao ser chamado a Thread que o chamou ficará presa até o cancelamento do registro.
	 * Assim é necessário a existência de duas thread, uma para fazer e outra para cancelar.
	 */

	native void register();

	/**
	 * Deve ser chamado para cancelar um registro feito por esse objeto na DLL.
	 * Assim que chamado irá cancelar qualquer recebimento de eventos para teclado.
	 */

	native void unregister();
}