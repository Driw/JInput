
#include <windows.h>

#include "Core.h"
#include "KeyEvent.h"

HINSTANCE instance = NULL;			// Inst�ncia pra "Singleton"

JavaVM *jvm = NULL;					// Java Virtual Machine
DWORD hookThreadId = 0;				// Thread que foi detectada o uso da DLL

jobject keyboardHookObject = NULL;	// Objeto que ser� usado contendo o processo de eventos
jmethodID processKeyMethod = NULL;	// M�todo que ser� chamado para processar o evento

extern "C"

#define DISPATCH_KV(env, key, transition, type) env->CallVoidMethod(keyboardHookObject, processKeyMethod, parseKeyEvent(env, key, transition, type));

/**
 * Chamado no momento em que o sistema detectar a utiliza��o dessa DLL.
 * @param hinstance refer�ncia da inst�ncia que utiliza a DLL.
 * @param reason qual a ras�o na utiliza��o da DLL.
 * @param reserved objeto reservado para outros afins.
 */
BOOL APIENTRY DllMain(HINSTANCE hinstance, DWORD reason, LPVOID reserved)
{
	switch (reason)
	{
		case DLL_PROCESS_ATTACH:
			instance = hinstance;
			break;
	}

	return TRUE;
}

/**
 * Procedimento chamado sempre que o sistema detectar um evento de teclado.
 * @param code c�digo da tecla do qual foi detectada pelo evento.
 * @param wParam qual o tipo de evento que est� sendo detectado.
 * @param lParam objeto contendo informa��es do evento.
 */
LRESULT CALLBACK LowLevelKeyboardProc(int code, WPARAM wParam, LPARAM lParam)
{
	JNIEnv* env;
	KBDLLHOOKSTRUCT* key = (KBDLLHOOKSTRUCT*) lParam;

	if (jvm->AttachCurrentThread((void **)&env, NULL) >= 0)
	{
		switch (wParam)
		{
			case WM_KEYDOWN:
			case WM_SYSKEYDOWN:
				DISPATCH_KV(env, key, true, KEY_TYPED);
				DISPATCH_KV(env, key, true, KEY_PRESSED);
				break;

			case WM_KEYUP:
			case WM_SYSKEYUP:
				DISPATCH_KV(env, key, false, KEY_RELEASED);
				break;
		}
	}

	return CallNextHookEx(NULL, code, wParam, lParam);
}

/**
 * Chamado sempre que for necess�rio fazer o registro de um novo servi�o para teclado.
 * Esse servi�o dever� ser salvo como um servi�o de escuta para eventos do teclado.
 * @param env refer�ncia do softwre do qual est� chamando esse procedimento.
 * @param obj refer�ncia do objeto no software que est� tentando registrar.
 * @param listener refer�ncia do objeto que ser� a escuta dos eventos.
 */
JNIEXPORT void JNICALL Java_org_diverproject_jni_input_KeyboardPicker_register(JNIEnv *env, jobject obj)
{
	HHOOK hookHandle = SetWindowsHookEx(WH_KEYBOARD_LL, LowLevelKeyboardProc, instance, 0);

	if (hookHandle == NULL)
		return;

	keyboardHookObject = env->NewGlobalRef(obj);

	jclass cls = env->GetObjectClass(keyboardHookObject);
	processKeyMethod = env->GetMethodID(cls, "receiveKey", "(Lorg/diverproject/jni/input/KeyEvent;)V");

	env->GetJavaVM(&jvm);
	hookThreadId = GetCurrentThreadId();

	MSG message;

	while (GetMessage(&message, NULL, 0, 0))
	{
		TranslateMessage(&message);
		DispatchMessage(&message);
	}
}

/**
 * Chamado para desfazer o registro de um servi�o para receber os eventos detecados do teclado.
 * Dever� interromper a thread do qual foi detecada como respons�vel pelo objeto de escuta.
 * @param env refer�ncia do softwre do qual est� chamando esse procedimento.
 * @param obj refer�ncia do objeto no software que est� tentando desfazer o registro.
 */
JNIEXPORT void JNICALL Java_org_diverproject_jni_input_KeyboardPicker_unregister(JNIEnv * env, jobject obj)
{
	if (hookThreadId == 0)
		return;

	PostThreadMessage(hookThreadId, WM_QUIT, 0, 0L);
}
