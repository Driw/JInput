
package org.diverproject.jni.input;

/**
 * <h1>Adaptador de Tecla</h1>
 *
 * <p>Classe usada para facilitar a codifica��o durante a utiliza��o de um KeyListener.
 * Tendo como finalidade apenas a redu��o de c�digo durante o desenvolvimento da aplica��o.
 * N�o deve ser usado como heran�a, para isso basta implementar KeyListener.</p>
 *
 * <p>As vezes ser� necess�rio usar um KeyListener diretamente no c�digo ao inv�s de uma
 * classe que o possua, usando essa classe, basta sobrescrever apenas os m�todos que
 * realmente ser�o necess�rios para aquela codifica��o em quest�o.</p>
 *
 * @author Andrew
 */

public final class KeyAdapter implements KeyListener
{
	@Override
	public void keyTyped(KeyEvent event)
	{
		
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		
	}
}
