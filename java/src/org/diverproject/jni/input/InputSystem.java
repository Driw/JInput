package org.diverproject.jni.input;

import org.diverproject.util.UtilException;
import org.diverproject.util.service.LibrarySystem;
import org.diverproject.util.service.Service;
import org.diverproject.util.service.ServiceException;
import org.diverproject.util.service.SystemBase;

/**
 * <h1>Sistema de Entrada</h1>
 *
 * <p>Esse sistema tem como finalidade garantir que os serviços de entrada funcionem.
 * Aqui deverá ser definido qual o serviço para despache do mouse e teclado.
 * Como também deverá garantir que estes sejam iniciados corretamente.</p>
 *
 * <p>Quando chamado pela primeira vez, irá inicializar a biblioteca do arquivo DLL.
 * Utilizando as seguintes informações:<br>
 * <b>Sistema para Carregar Bibliotecas:</b>: <class>LibrarySystem</class><br>
 * <b>Nome da Biblioteca que será carregada: <code>jniInput</code><br>
 * <b>Caminho da Propriedade na JVM:</b> org.diverproject.librarypath.</p>
 *
 * <p>Possui getters e setters correspondentes aos serviços de despache de entrada.
 * Podendo ainda inicializar ou desligar os serviços que nele já foram definidos.
 * Usando as funcionalidades de um serviço de modo que este funcione durante trocas.</p>
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
	 * Referência do objeto InputSystem para adaptar ao padrão de projetos Singleton.
	 */
	private static final InputSystem INSTANCE = new InputSystem();

	/**
	 * Chamado quando essa classe for chamada pela primeira vez no sistema.
	 * Deverá garantir que a biblioteca (DLL) seja carregada adequadamente.
	 * O carregamento já possui um padrão pré-definido e deve usar o mesmo.
	 * Caso a inicialização não possa ser atendida os serviços não vão funcionar.
	 */

	static
	{
		LibrarySystem ls = LibrarySystem.getInstance();
		ls.load("jniInput", "org.diverproject.librarypath");
	}

	/**
	 * Objeto que irá manter a conexão da aplicação com a biblioteca.
	 */
	private KeyboardHook keyboardPoolHook;

	/**
	 * Referência do despachante para eventos de teclado.
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
	 * Quando chamado deverá tentar fazer a inicialização de todos os serviços de entrada.
	 * Caso um serviço não esteja definido ele será ignorado passando par ao seguinte.
	 * Se houver um serviço irá inicializá-lo, se tiver sido iniciado irá tentar reiniciar.
	 * @throws InputException apenas se alguma condição acima não puder ser atendida.
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
	 * Despachante para teclado é um serviço que irá receber os eventos de teclado.
	 * @return aquisição do atual despachante para eventos de teclado.
	 */

	public KeyboardDispatcher getKeyboardDispatcher()
	{
		return keyboardDispatcher;
	}

	/**
	 * Permite definir qual será o novo despachante de eventos para teclado a ser usado.
	 * @param dispatcher referência do objeto que será o novo despachante para teclado.
	 * @throws InputException apenas se já houver um e este não puder ser interrompido.
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
	 * Essa classe utiliza o padrão de projeto Singleton onde só pode existir
	 * apenas uma instância desse objeto, não sendo possível criar uma outra.
	 * Esse método permite obter essa única instância possível de existência.
	 * @return referência do sistema para gerenciamento de entradas.
	 */

	public static InputSystem getInstance()
	{
		return INSTANCE;
	}
}
