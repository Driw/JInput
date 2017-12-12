
package org.diverproject.jni.input;

import static org.diverproject.jni.input.enums.EnumCH.*;
import static org.diverproject.jni.input.enums.EnumKEY.*;
import static org.diverproject.jni.input.enums.EnumVK.*;

import org.diverproject.util.BitWise;
import org.diverproject.util.ObjectDescription;

/**
 * <h1>Evento de Tecla</h1>
 *
 * <p>Esse evento na biblioteca JNI Input � criado internamente na codifica��o da DLL.
 * Sempre que uma tecla interagir com o sistema operacional ela ser� desencadeada.
 * Para esse evento h� tr�s tipos de a��es que podem ser criadas ao usar o teclado.</p>
 *
 * <p>A primeira � o digitar, que ir� carregar com sigo um caracter, que pode ser usado
 * para que o software desenvolvido consiga exibir algo na tela como uma digita��o.
 * Para melhor entendimento podemos imaginar a ideia de usar isso em um campo de texto.</p>
 *
 * <p>A segunda sempre ir� aparecer logo ap�s a primeira (digitar) que � o de pressionar.
 * Em quanto uma tecla for pressionada ambas as a��es ser�o chamadas em quanto poss�vel.
 * Assim sendo, as a��es ir�o alterar entre digitar e pressionar durante a detec��o.</p>
 *
 * <p>A terceira a��o s� ir� aparecer quando uma tecla parar de ser pressionada.
 * Al�m disso ser� chamada apenas uma vez, diferente das outras duas primeiras,
 * que podem aparecer mais de uma vez de acordo com o tempo que forem pressionadas.</p>
 *
 * <p>Para esse evento s�o designados alguns atributos e propriedades que ser�o �teis.
 * Quanto aos atributos s�o referentes ao c�digo virtual (origem) e o c�digo no JNI.
 * O c�digo no JNI � um diferente de origem (Windows), e por �ltimo o caracter.
 * Nem todos eventos possuem um caracter, depende da a��o e da tecla que o acionou.</p>
 *
 * <p>As propriedades ir�o apenas definir algumas extens�es em rela��o ao estado do teclado.
 * Como por exemplo a ativa��o do <code>CAPITAL</code> ativado ao clicar com o Caps Lock.
 * Al�m desse h� de alt, control e shift pressionadas ou se a tecla est� pressionada ou n�o.</p>
 *
 * @author Andrew
 */

public class KeyEvent extends InputEvent
{
	/**
	 * C�digo que ir� determinara a a��o do evento como <b>Tecla Digitada</b>.
	 */
	public static final int KT_TYPED = 1;

	/**
	 * C�digo que ir� determinara a a��o do evento como <b>Tecla Pressionada</b>.
	 */
	public static final int KT_PRESSED = 2;

	/**
	 * C�digo que ir� determinara a a��o do evento como <b>Tecla Liberada</b>.
	 */
	public static final int KT_RELEASED = 3;

	/**
	 * Vetor contendo o nome de todos os tipos de a��es que o evento pode gerar.
	 */
	public static final String KEY_TYPES[] = new String[]
	{
		"UNKNOW", "TYPED", "PRESSED", "RELEASED"
	};


	/**
	 * C�digo que ir� determinar a propriedade do teclado como <b>CapsLock ativo</b>.
	 */
	public static final int KP_CAPITAL = 0x01;

	/**
	 * C�digo que ir� determinar a propriedade do teclado como <b>Extens�o Esquerda ativa</b>.
	 */
	public static final int KP_LEFT = 0x02;

	/**
	 * C�digo que ir� determinar a propriedade do teclado como <b>Extens�o Direita ativa</b>.
	 */
	public static final int KP_RIGHT = 0x04;

	/**
	 * C�digo que ir� determinar a propriedade do teclado como <b>SHIFT ativo</b>.
	 */
	public static final int KP_SHIFT = 0x08;

	/**
	 * C�digo que ir� determinar a propriedade do teclado como <b>CTRL ativo</b>.
	 */
	public static final int KP_CONTROL = 0x10;

	/**
	 * C�digo que ir� determinar a propriedade do teclado como <b>ALT ativo</b>.
	 */
	public static final int KP_ALTERNATIVE = 0x20;

	/**
	 * Vetor contendo o nome de todas as propriedades que o teclado pode possuir.
	 */
	public static final String KEY_PROPERTIES[] = new String[]
	{
		"CAPITAL", "LEFT", "RIGHT", "SHIFT", "CONTROL", "ALTERNATVE",
	};


	/**
	 * C�digo da tecla no Windows que criou o evento (fonte).
	 */
	private int vk;

	/**
	 * C�digo na biblioteca da tecla que criou o evento (personalizado).
	 */
	private int key;

	/**
	 * Caracter vinculado a tecla digitada de acordo com as condi��es do teclado.
	 */
	private char ch;

	/**
	 * Propriedades respectivas as condi��es do teclado durante o evento.
	 */
	private BitWise properties;

	/**
	 * Constr�i um novo evento para teclas do teclado, sendo necess�rio passar:
	 * @param type qual o tipo de a��o que ser� usado (<code>KT</code>).
	 * @param vk c�digo da tecla virtual que gerou o evento (<code>VK</code>).
	 * @param key codigo na biblioteca da tecla que gerou o evento (<code>KEY</code>).
	 * @param ch caracter que apareceu como digita��o para o evento (<code>CH</code>).
	 * @param properties valor contendo as propriedades no formato <class>BitWise</class>.
	 */

	public KeyEvent(int type, int vk, int key, char ch, int properties)
	{
		super(EV_KEYBOARD, type);

		this.ch = ch;
		this.vk = vk;
		this.key = key;
		this.properties = new BitWise(KEY_PROPERTIES);
	}

	/**
	 * N�o recomend�vel utilizar, por�m determina qual foi a tecla usada pelo evento.
	 * @return aquisi��o do c�digo da tecla desse evento na documenta��o do Windows.
	 */

	public int getVirtualKey()
	{
		return vk;
	}

	/**
	 * Principal item do evento que ir� dizer qual a tecla que desencadeou o evento.
	 * @return aquisi��o do c�digo da tecla desse evento na biblioteca JNI Input.
	 */

	public int getKey()
	{
		return key;
	}

	/**
	 * Usado apenas por eventos do tipo digita��o e apenas para algumas teclas.
	 * @return caracter vinculado ao evento desencadeado ou ent�o <code>CH_NULL</code>,
	 * caso n�o haja uma vinculado a tecla ou n�o seja do tipo digita��o.
	 */

	public char getChar()
	{
		return ch;
	}

	/**
	 * Para cada tecla existente no teclado foi definido um nome para o mesmo.
	 * Usado em <code>toString()</code> para facilitar a visualiza��o da tecla usada.
	 * <i>Neste caso � referente ao nome original dado pela documenta��o da Microsoft.</i>
	 * @return aquisi��o do nome que foi dado a tecla usada por esse evento.
	 */

	public String getVirtualKeyName()
	{
		return VIRTUAL_KEY_NAME[vk];
	}

	/**
	 * Para cada tecla existente no teclado foi definido um nome para o mesmo.
	 * Usado em <code>toString()</code> para facilitar a visualiza��o da tecla usada.
	 * <i>Nesse caso � referente ao nome dado pela Diver Project na biblioteca.</i>
	 * @return aquisi��o do nome que foi dado a tecla usada por esse evento.
	 */

	public String getKeyName()
	{
		return KEY_NAME[key];
	}

	/**
	 * Todos os caracteres possui um nome, utilizado por <code>toString()</code>,
	 * de modo que o caracter possa ser reconhecido mais facilmente.
	 * Ir� funcionar apenas com <code>KT_TYPED</code> que possui um vinculado.
	 * @return aquisi��o do nome do caracter que essa a��o possui.
	 */

	public String getCharName()
	{
		return CHARACTER_NAME[ch];
	}

	/**
	 * Deve verificar se h� uma tecla ALT pressionada independente do lado.
	 * Para distinguir o lado dever� ser usado <code>isLeft()</code> ou <code>isRight()</code>
	 * @return true se houver qualquer uma das duas teclas ALT pressionadas.
	 */

	public boolean hasAlt()
	{
		return properties.is(KP_ALTERNATIVE);
	}

	/**
	 * Deve verificar se h� uma tecla CTRL pressionada independente do lado.
	 * Para distinguir o lado dever� ser usado <code>isLeft()</code> ou <code>isRight()</code>
	 * @return true se houver qualquer uma das duas teclas CTRL pressionadas.
	 */

	public boolean hasCtrl()
	{
		return properties.is(KP_CONTROL);
	}

	/**
	 * Deve verificar se h� uma tecla SHIFT pressionada independente do lado.
	 * Para distinguir o lado dever� ser usado <code>isLeft()</code> ou <code>isRight()</code>
	 * @return true se houver qualquer uma das duas teclas SHIFT pressionadas.
	 */

	public boolean hasShift()
	{
		return properties.is(KP_SHIFT);
	}

	/**
	 * Deve verificar se a tecla de extens�o usada � a do lado esquerdo.
	 * Isso ir� indicar que pode ser uma tecla de extens�o, conhecidas
	 * como ALT, CTRL, SHIFT e WIN (apenas as do lado esquerdo do teclado).
	 * @return true se for do lado esquerdo ou false caso contr�rio.
	 */

	public boolean isLeft()
	{
		return properties.is(KP_LEFT);
	}

	/**
	 * Deve verificar se a tecla de extens�o usada � a do lado direito.
	 * Isso ir� indicar que pode ser uma tecla de extens�o, conhecidas
	 * como ALT, CTRL, SHIFT e WIN (apenas as do lado direito do teclado).
	 * @return true se for do lado direito ou false caso contr�rio.
	 */

	public boolean isRight()
	{
		return properties.is(KP_RIGHT);
	}

	/**
	 * Deve verificar como se encontra o estado de transi��o do evento.
	 * Para o estado de transi��o estar ativo no evento, o mesmo dever�
	 * encontrar-se no estado de pressionado ou ser um evento de digita��o.
	 * @return true se a tecla estiver abaixada ou false caso contr�rio.
	 */

	public boolean isTransition()
	{
		return getType() != KT_RELEASED;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof KeyEvent)
		{
			KeyEvent event = (KeyEvent) obj;

			return	event.vk == vk && event.properties.getValue() == properties.getValue();
		}
		
		return false;
	}

	@Override
	public String getIDString()
	{
		return "KEYBOARD";
	}

	@Override
	public String getTypeString()
	{
		return KEY_TYPES[getType()];
	}

	@Override
	protected void toString(ObjectDescription description)
	{
		description.append("key", key);
		description.append("virtual", vk);
		description.append("character", ch);
		description.append("keyName", getKeyName());
		description.append("virtualName", getVirtualKeyName());
		description.append("characterName", getCharName());
		description.append("properties", properties.toStringProperties());
	}
}