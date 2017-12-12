package org.diverproject.jni.input;

/**
 * <h1>Gancho para Keyboard</h1>
 *
 * <p>Thread que será responsável por garantir que o sistema mantenha a conexão com a DLL.
 * A forma como é feito a conexão com a DLL necessita que uma thread mantenha-se nela.
 * Sendo necessário assim que haja uma nova thread, se a ThreadMain for associada a DLL,
 * o sistema irá "travar", pois a DLL só fará a liberação através de outra Thread.</p>
 *
 * <p>Tem como finalidade apenas garantir que quando iniciado registre a Thread na DLL.
 * Como ainda também deve permitir desfazer o registro da Thread liberando o mesmo.</p>
 *
 * @see KeyboardPicker
 *
 * @author Andrew
 */

class KeyboardHook extends Thread
{
	/**
	 * Referência do objeto que irá receber os eventos de teclado direto da DLL.
	 */
	private KeyboardPicker picker;

	/**
	 * Constrói uma nova thread para manter o registro da aplicação com a DLL.
	 * Essa thread será automaticamente encerrada junto com a Thread Main.
	 */

	KeyboardHook()
	{
		this.setDaemon(true);
		this.setName("JNI KPoolHook");
	}

	@Override
	public void run()
	{
		picker = new KeyboardPicker();
		picker.register();
	}

	/**
	 * Quando chamado deverá dizer ao apanhador para teclado desfazer o registro.
	 * Uma vez chamado a thread terminará seu loop dentro do registro na DLL.
	 * Como também irá fazer com que o apanhador para teclado pare de receber eventos.
	 */

	public void shutdown()
	{
		if (picker != null)
		{
			picker.unregister();
			picker = null;
		}
	}
}
