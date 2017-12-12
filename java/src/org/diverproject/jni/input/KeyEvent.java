
package org.diverproject.jni.input;

import static org.diverproject.jni.input.enums.EnumCH.*;
import static org.diverproject.jni.input.enums.EnumKEY.*;
import static org.diverproject.jni.input.enums.EnumVK.*;

import org.diverproject.util.BitWise;
import org.diverproject.util.ObjectDescription;

/**
 * <h1>Evento de Tecla</h1>
 *
 * <p>Esse evento na biblioteca JNI Input é criado internamente na codificação da DLL.
 * Sempre que uma tecla interagir com o sistema operacional ela será desencadeada.
 * Para esse evento há três tipos de ações que podem ser criadas ao usar o teclado.</p>
 *
 * <p>A primeira é o digitar, que irá carregar com sigo um caracter, que pode ser usado
 * para que o software desenvolvido consiga exibir algo na tela como uma digitação.
 * Para melhor entendimento podemos imaginar a ideia de usar isso em um campo de texto.</p>
 *
 * <p>A segunda sempre irá aparecer logo após a primeira (digitar) que é o de pressionar.
 * Em quanto uma tecla for pressionada ambas as ações serão chamadas em quanto possível.
 * Assim sendo, as ações irão alterar entre digitar e pressionar durante a detecção.</p>
 *
 * <p>A terceira ação só irá aparecer quando uma tecla parar de ser pressionada.
 * Além disso será chamada apenas uma vez, diferente das outras duas primeiras,
 * que podem aparecer mais de uma vez de acordo com o tempo que forem pressionadas.</p>
 *
 * <p>Para esse evento são designados alguns atributos e propriedades que serão úteis.
 * Quanto aos atributos são referentes ao código virtual (origem) e o código no JNI.
 * O código no JNI é um diferente de origem (Windows), e por último o caracter.
 * Nem todos eventos possuem um caracter, depende da ação e da tecla que o acionou.</p>
 *
 * <p>As propriedades irão apenas definir algumas extensões em relação ao estado do teclado.
 * Como por exemplo a ativação do <code>CAPITAL</code> ativado ao clicar com o Caps Lock.
 * Além desse há de alt, control e shift pressionadas ou se a tecla está pressionada ou não.</p>
 *
 * @author Andrew
 */

public class KeyEvent extends InputEvent
{
	/**
	 * Código que irá determinara a ação do evento como <b>Tecla Digitada</b>.
	 */
	public static final int KT_TYPED = 1;

	/**
	 * Código que irá determinara a ação do evento como <b>Tecla Pressionada</b>.
	 */
	public static final int KT_PRESSED = 2;

	/**
	 * Código que irá determinara a ação do evento como <b>Tecla Liberada</b>.
	 */
	public static final int KT_RELEASED = 3;

	/**
	 * Vetor contendo o nome de todos os tipos de ações que o evento pode gerar.
	 */
	public static final String KEY_TYPES[] = new String[]
	{
		"UNKNOW", "TYPED", "PRESSED", "RELEASED"
	};


	/**
	 * Código que irá determinar a propriedade do teclado como <b>CapsLock ativo</b>.
	 */
	public static final int KP_CAPITAL = 0x01;

	/**
	 * Código que irá determinar a propriedade do teclado como <b>Extensão Esquerda ativa</b>.
	 */
	public static final int KP_LEFT = 0x02;

	/**
	 * Código que irá determinar a propriedade do teclado como <b>Extensão Direita ativa</b>.
	 */
	public static final int KP_RIGHT = 0x04;

	/**
	 * Código que irá determinar a propriedade do teclado como <b>SHIFT ativo</b>.
	 */
	public static final int KP_SHIFT = 0x08;

	/**
	 * Código que irá determinar a propriedade do teclado como <b>CTRL ativo</b>.
	 */
	public static final int KP_CONTROL = 0x10;

	/**
	 * Código que irá determinar a propriedade do teclado como <b>ALT ativo</b>.
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
	 * Código da tecla no Windows que criou o evento (fonte).
	 */
	private int vk;

	/**
	 * Código na biblioteca da tecla que criou o evento (personalizado).
	 */
	private int key;

	/**
	 * Caracter vinculado a tecla digitada de acordo com as condições do teclado.
	 */
	private char ch;

	/**
	 * Propriedades respectivas as condições do teclado durante o evento.
	 */
	private BitWise properties;

	/**
	 * Constrói um novo evento para teclas do teclado, sendo necessário passar:
	 * @param type qual o tipo de ação que será usado (<code>KT</code>).
	 * @param vk código da tecla virtual que gerou o evento (<code>VK</code>).
	 * @param key codigo na biblioteca da tecla que gerou o evento (<code>KEY</code>).
	 * @param ch caracter que apareceu como digitação para o evento (<code>CH</code>).
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
	 * Não recomendável utilizar, porém determina qual foi a tecla usada pelo evento.
	 * @return aquisição do código da tecla desse evento na documentação do Windows.
	 */

	public int getVirtualKey()
	{
		return vk;
	}

	/**
	 * Principal item do evento que irá dizer qual a tecla que desencadeou o evento.
	 * @return aquisição do código da tecla desse evento na biblioteca JNI Input.
	 */

	public int getKey()
	{
		return key;
	}

	/**
	 * Usado apenas por eventos do tipo digitação e apenas para algumas teclas.
	 * @return caracter vinculado ao evento desencadeado ou então <code>CH_NULL</code>,
	 * caso não haja uma vinculado a tecla ou não seja do tipo digitação.
	 */

	public char getChar()
	{
		return ch;
	}

	/**
	 * Para cada tecla existente no teclado foi definido um nome para o mesmo.
	 * Usado em <code>toString()</code> para facilitar a visualização da tecla usada.
	 * <i>Neste caso é referente ao nome original dado pela documentação da Microsoft.</i>
	 * @return aquisição do nome que foi dado a tecla usada por esse evento.
	 */

	public String getVirtualKeyName()
	{
		return VIRTUAL_KEY_NAME[vk];
	}

	/**
	 * Para cada tecla existente no teclado foi definido um nome para o mesmo.
	 * Usado em <code>toString()</code> para facilitar a visualização da tecla usada.
	 * <i>Nesse caso é referente ao nome dado pela Diver Project na biblioteca.</i>
	 * @return aquisição do nome que foi dado a tecla usada por esse evento.
	 */

	public String getKeyName()
	{
		return KEY_NAME[key];
	}

	/**
	 * Todos os caracteres possui um nome, utilizado por <code>toString()</code>,
	 * de modo que o caracter possa ser reconhecido mais facilmente.
	 * Irá funcionar apenas com <code>KT_TYPED</code> que possui um vinculado.
	 * @return aquisição do nome do caracter que essa ação possui.
	 */

	public String getCharName()
	{
		return CHARACTER_NAME[ch];
	}

	/**
	 * Deve verificar se há uma tecla ALT pressionada independente do lado.
	 * Para distinguir o lado deverá ser usado <code>isLeft()</code> ou <code>isRight()</code>
	 * @return true se houver qualquer uma das duas teclas ALT pressionadas.
	 */

	public boolean hasAlt()
	{
		return properties.is(KP_ALTERNATIVE);
	}

	/**
	 * Deve verificar se há uma tecla CTRL pressionada independente do lado.
	 * Para distinguir o lado deverá ser usado <code>isLeft()</code> ou <code>isRight()</code>
	 * @return true se houver qualquer uma das duas teclas CTRL pressionadas.
	 */

	public boolean hasCtrl()
	{
		return properties.is(KP_CONTROL);
	}

	/**
	 * Deve verificar se há uma tecla SHIFT pressionada independente do lado.
	 * Para distinguir o lado deverá ser usado <code>isLeft()</code> ou <code>isRight()</code>
	 * @return true se houver qualquer uma das duas teclas SHIFT pressionadas.
	 */

	public boolean hasShift()
	{
		return properties.is(KP_SHIFT);
	}

	/**
	 * Deve verificar se a tecla de extensão usada é a do lado esquerdo.
	 * Isso irá indicar que pode ser uma tecla de extensão, conhecidas
	 * como ALT, CTRL, SHIFT e WIN (apenas as do lado esquerdo do teclado).
	 * @return true se for do lado esquerdo ou false caso contrário.
	 */

	public boolean isLeft()
	{
		return properties.is(KP_LEFT);
	}

	/**
	 * Deve verificar se a tecla de extensão usada é a do lado direito.
	 * Isso irá indicar que pode ser uma tecla de extensão, conhecidas
	 * como ALT, CTRL, SHIFT e WIN (apenas as do lado direito do teclado).
	 * @return true se for do lado direito ou false caso contrário.
	 */

	public boolean isRight()
	{
		return properties.is(KP_RIGHT);
	}

	/**
	 * Deve verificar como se encontra o estado de transição do evento.
	 * Para o estado de transição estar ativo no evento, o mesmo deverá
	 * encontrar-se no estado de pressionado ou ser um evento de digitação.
	 * @return true se a tecla estiver abaixada ou false caso contrário.
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