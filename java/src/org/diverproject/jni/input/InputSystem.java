package org.diverproject.jni.input;

import org.diverproject.util.UtilException;
import org.diverproject.util.service.LibrarySystem;
import org.diverproject.util.service.Service;
import org.diverproject.util.service.ServiceException;
import org.diverproject.util.service.SystemBase;

/**
 * <h1>Sistema de Entrada</h1>
 *
 * <p>Esse sistema tem como finalidade garantir que os servi�os de entrada funcionem.
 * Aqui dever� ser definido qual o servi�o para despache do mouse e teclado.
 * Como tamb�m dever� garantir que estes sejam iniciados corretamente.</p>
 *
 * <p>Quando chamado pela primeira vez, ir� inicializar a biblioteca do arquivo DLL.
 * Utilizando as seguintes informa��es:<br>
 * <b>Sistema para Carregar Bibliotecas:</b>: <class>LibrarySystem</class><br>
 * <b>Nome da Biblioteca que ser� carregada: <code>jniInput</code><br>
 * <b>Caminho da Propriedade na JVM:</b> org.diverproject.librarypath.</p>
 *
 * <p>Possui getters e setters correspondentes aos servi�os de despache de entrada.
 * Podendo ainda inicializar ou desligar os servi�os que nele j� foram definidos.
 * Usando as funcionalidades de um servi�o de modo que este funcione durante trocas.</p>
 *
 * @see SystemBase
 * @see KeyboardHook
 * @see KeyboardDispatcher
 *
 * @author Andrew
 */

public class InputSystem extends SystemBase
{
	/**
	 * Refer�ncia do objeto InputSystem para adaptar ao padr�o de projetos Singleton.
	 */
	private static final InputSystem INSTANCE = new InputSystem();

	/**
	 * Chamado quando essa classe for chamada pela primeira vez no sistema.
	 * Dever� garantir que a biblioteca (DLL) seja carregada adequadamente.
	 * O carregamento j� possui um padr�o pr�-definido e deve usar o mesmo.
	 * Caso a inicializa��o n�o possa ser atendida os servi�os n�o v�o funcionar.
	 */

	static
	{
		LibrarySystem ls = LibrarySystem.getInstance();
		ls.load("jniInput", "org.diverproject.librarypath");
	}

	/**
	 * Objeto que ir� manter a conex�o da aplica��o com a biblioteca.
	 */
	private KeyboardHook keyboardPoolHook;

	/**
	 * Refer�ncia do despachante para eventos de teclado.
	 */
	private KeyboardDispatcher keyboardDispatcher;

	@Override
	public String getSystemName()
	{
		return "JNI.Input";
	}

	@Override
	public void update(long delay)
	{
		if (keyboardDispatcher != null)
			keyboardDispatcher.update(delay);
	}

	@Override
	public void shutdown() throws InputException
	{
		try {

			if (keyboardDispatcher != null)
			{
				keyboardDispatcher.terminate();
				keyboardDispatcher = null;
			}

		} catch (UtilException e) {
			throw new InputException(e.getMessage());
		}

		if (keyboardPoolHook != null)
		{
			keyboardPoolHook.shutdown();
			keyboardPoolHook = null;
		}
	}

	/**
	 * Quando chamado dever� tentar fazer a inicializa��o de todos os servi�os de entrada.
	 * Caso um servi�o n�o esteja definido ele ser� ignorado passando par ao seguinte.
	 * Se houver um servi�o ir� inicializ�-lo, se tiver sido iniciado ir� tentar reiniciar.
	 * @throws InputException apenas se alguma condi��o acima n�o puder ser atendida.
	 */

	public void initialize() throws InputException
	{
		if (keyboardDispatcher != null)
		{
			if (keyboardDispatcher.getState() != Service.SERVICE_UNDEFINID)
				throw new InputException("KeyboardDispatcher fora do estado esperado");

		try {
			keyboardDispatcher.start();
		} catch (UtilException e) {
			throw new InputException(e.getMessage());
		}

			keyboardPoolHook = new KeyboardHook();
			keyboardPoolHook.start();
		}
	}

	/**
	 * Despachante para teclado � um servi�o que ir� receber os eventos de teclado.
	 * @return aquisi��o do atual despachante para eventos de teclado.
	 */

	public KeyboardDispatcher getKeyboardDispatcher()
	{
		return keyboardDispatcher;
	}

	/**
	 * Permite definir qual ser� o novo despachante de eventos para teclado a ser usado.
	 * @param dispatcher refer�ncia do objeto que ser� o novo despachante para teclado.
	 * @throws InputException apenas se j� houver um e este n�o puder ser interrompido.
	 */

	public void setKeyboardDispatcher(KeyboardDispatcher dispatcher) throws InputException
	{
		try {

			if (keyboardDispatcher != null)
				keyboardDispatcher.interrupted();

		} catch (ServiceException e) {
			throw new InputException(e.getMessage());
		}

		keyboardDispatcher = dispatcher;
	}

	/**
	 * Essa classe utiliza o padr�o de projeto Singleton onde s� pode existir
	 * apenas uma inst�ncia desse objeto, n�o sendo poss�vel criar uma outra.
	 * Esse m�todo permite obter essa �nica inst�ncia poss�vel de exist�ncia.
	 * @return refer�ncia do sistema para gerenciamento de entradas.
	 */

	public static InputSystem getInstance()
	{
		return INSTANCE;
	}
}
