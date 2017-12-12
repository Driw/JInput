package org.diverproject.jni.input;

import org.diverproject.util.Util;

/**
 * <h1>Processador para Teclado</h1>
 *
 * <p>Esse processador dever� verificar se h� eventos na lista de espera.
 * Se houver eventos, dever� chamar o sistema de entradas e obter o
 * despachante para teclado, iniciando o despache da lista de espera.</p>
 *
 * <p>Uma vez que um evento tenha sido despachado para o servi�o respectivo,
 * esse evento ser� removido da lista de espera, evitando loop infinito.
 * Novos eventos sempre ser�o posicionados ao final da lista.</p>
 *
 * @see Thread
 * @see KeyboardPicker
 *
 * @author Andrew
 */

class KeyboardProcessor extends Thread
{
	/**
	 * Apanhador para teclado que ser� usado como receptor dos eventos.
	 */
	private KeyboardPicker picker;

	/**
	 * Constr�i um novo processador para teclado sendo necess�rio definir:
	 * @param picker apanhador para teclados que ser� usado para fazer o despache.
	 */
	
	KeyboardProcessor(KeyboardPicker picker)
	{
		this.setDaemon(true);
		this.setName("JNI KEventProcessor");
		this.picker = picker;
	}

	@Override
	public void run()
	{
		InputSystem system = InputSystem.getInstance();

		while (true)
		{
			synchronized (picker.buffer)
			{
				if (!picker.buffer.isEmpty())
				{
					KeyEvent event = picker.buffer.poll();
					KeyboardDispatcher keyboard = system.getKeyboardDispatcher();
					keyboard.dispatch(event);
				}

				else
					Util.sleep(1);
			}
		}
	}
}

