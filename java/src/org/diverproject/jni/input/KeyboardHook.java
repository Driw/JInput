package org.diverproject.jni.input;

/**
 * <h1>Gancho para Keyboard</h1>
 *
 * <p>Thread que ser� respons�vel por garantir que o sistema mantenha a conex�o com a DLL.
 * A forma como � feito a conex�o com a DLL necessita que uma thread mantenha-se nela.
 * Sendo necess�rio assim que haja uma nova thread, se a ThreadMain for associada a DLL,
 * o sistema ir� "travar", pois a DLL s� far� a libera��o atrav�s de outra Thread.</p>
 *
 * <p>Tem como finalidade apenas garantir que quando iniciado registre a Thread na DLL.
 * Como ainda tamb�m deve permitir desfazer o registro da Thread liberando o mesmo.</p>
 *
 * @see KeyboardPicker
 *
 * @author Andrew
 */

class KeyboardHook extends Thread
{
	/**
	 * Refer�ncia do objeto que ir� receber os eventos de teclado direto da DLL.
	 */
	private KeyboardPicker picker;

	/**
	 * Constr�i uma nova thread para manter o registro da aplica��o com a DLL.
	 * Essa thread ser� automaticamente encerrada junto com a Thread Main.
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
	 * Quando chamado dever� dizer ao apanhador para teclado desfazer o registro.
	 * Uma vez chamado a thread terminar� seu loop dentro do registro na DLL.
	 * Como tamb�m ir� fazer com que o apanhador para teclado pare de receber eventos.
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
