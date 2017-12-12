
#include <windows.h>

#include "KeyEvent.h"

static int properties;

#define IS_SHIFT ((properties & KP_SHIFT) == KP_SHIFT)
#define IS_CAPSLOCK ((properties & KP_CAPITAL) == KP_CAPITAL)
#define IS_ALTERNATIVE ((properties & KP_ALTERNATIVE) == KP_ALTERNATIVE)

#define ALTERNATIVE(first, second, third) (IS_SHIFT ? (IS_ALTERNATIVE ? third : second) : first)
#define CASE(lower, upper) (IS_CAPSLOCK ? (IS_SHIFT ? lower : upper) : (IS_SHIFT ? upper : lower))

jobject parseKeyEvent(JNIEnv * env, KBDLLHOOKSTRUCT * key, jboolean transition, jint type)
{
	int vk = key->vkCode;
	jint code = KEY_UNDEFINED;
	jchar ch = CH_NULL;

	if (transition)
		properties |= KP_TRANSITION;
	else
		properties -= properties & KP_TRANSITION;

	switch (vk)
	{
		case WK_CLEAR:					return NULL;	break;
		case WK_KANA:					return NULL;	break;
		case WK_JUNJA:					return NULL;	break;
		case WK_FINAL:					return NULL;	break;
		case WK_HANJA:					return NULL;	break;
		case WK_CONVERT:				return NULL;	break;
		case WK_NONCONVERT:				return NULL;	break;
		case WK_ACCEPT:					return NULL;	break;
		case WK_MODECHANGE:				return NULL;	break;
		case WK_SELECT:					return NULL;	break;
		case WK_PRINT:					return NULL;	break;
		case WK_EXECUTE:				return NULL;	break;
		case WK_HELP:					return NULL;	break;
		case WK_SLEEP:					return NULL;	break;
		case WK_BROWSER_BACK:			return NULL;	break;
		case WK_BROWSER_FORWARD:		return NULL;	break;
		case WK_BROWSER_REFRESH:		return NULL;	break;
		case WK_BROWSER_STOP:			return NULL;	break;
		case WK_BROWSER_SEARCH:			return NULL;	break;
		case WK_BROWSER_FAVORITES:		return NULL;	break;
		case WK_BROWSER_HOME:			return NULL;	break;
		case WK_VOLUME_MUTE:			return NULL;	break;
		case WK_VOLUME_DOWN:			return NULL;	break;
		case WK_VOLUME_UP:				return NULL;	break;
		case WK_MEDIA_NEXT_TRACK:		return NULL;	break;
		case WK_MEDIA_PREV_TRACK:		return NULL;	break;
		case WK_MEDIA_STOP:				return NULL;	break;
		case WK_MEDIA_PLAY_PAUSE:		return NULL;	break;
		case WK_LAUNCH_MAIL:			return NULL;	break;
		case WK_LAUNCH_MEDIA_SELECT:	return NULL;	break;
		case WK_LAUNCH_APP1:			return NULL;	break;
		case WK_LAUNCH_APP2:			return NULL;	break;
		case WK_PROCESSKEY:				return NULL;	break;
		case WK_PACKET:					return NULL;	break;
		case WK_ATTN:					return NULL;	break;
		case WK_CRSEL:					return NULL;	break;
		case WK_EXSEL:					return NULL;	break;
		case WK_EREOF:					return NULL;	break;
		case WK_PLAY:					return NULL;	break;
		case WK_ZOOM:					return NULL;	break;
		case WK_NONAME:					return NULL;	break;
		case WK_PA1:					return NULL;	break;
		case WK_OEM_8:					return NULL;	break;
		case WK_OEM_CLEAR:				return NULL;	break;

		case WK_TAB:					code = KEY_TAB;				ch = CH_HORIZONTAL_TABULATION;	break;
		case WK_RETURN:					code = KEY_ENTER;			ch = CH_NEW_LINE;				break;
		case WK_SPACE:					code = KEY_SPACE;			ch = CH_SPACE;					break;

		case WK_SHIFT:					code = KEY_LEFT_SHIFT;		break;
		case WK_CONTROL:				code = KEY_LEFT_CONTROL;	break;
		case WK_MENU:					code = KEY_LEFT_ALT;		break;

		case WK_BACK:					code = KEY_BACKSPACE;		break;
		case WK_PAUSE:					code = KEY_PAUSE_BREAK;		break;
		case WK_ESCAPE:					code = KEY_ESCAPE;			break;
		case WK_PRIOR:					code = KEY_PAGE_UP;			break;
		case WK_NEXT:					code = KEY_PAGE_DOWN;		break;
		case WK_END:					code = KEY_END;				break;
		case WK_HOME:					code = KEY_HOME;			break;
		case WK_LEFT:					code = KEY_LEFT;			break;
		case WK_UP:						code = KEY_UP;				break;
		case WK_RIGHT:					code = KEY_RIGHT;			break;
		case WK_DOWN:					code = KEY_DOWN;			break;
		case WK_SNAPSHOT:				code = KEY_PRINT_SCREEN;	break;
		case WK_INSERT:					code = KEY_INSERT;			break;
		case WK_DELETE:					code = KEY_DELETE;			break;
		case WK_NUMLOCK:				code = KEY_NUMLOCK;			break;
		case WK_SCROLL:					code = KEY_SCROLL_LOCK;		break;
		case WK_LWIN:					code = KEY_LEFT_WINDOW;		break;
		case WK_RWIN:					code = KEY_RIGHT_WINDOW;	break;
		case WK_APPS:					code = KEY_APPLICATIONS;	break;

		case WK_CAPITAL:				code = KEY_CAPSLOCK;		transition ? properties |= KP_CAPITAL : properties -= (properties & KP_CAPITAL);												break;
		case WK_LSHIFT:					code = KEY_LEFT_SHIFT;		transition ? properties |= KP_SHIFT | KP_LEFT		 : properties -= (properties & KP_SHIFT) + (properties & KP_LEFT);			break;
		case WK_RSHIFT:					code = KEY_RIGHT_SHIFT;		transition ? properties |= KP_SHIFT | KP_RIGHT		 : properties -= (properties & KP_SHIFT) + (properties & KP_RIGHT);			break;
		case WK_LCONTROL:				code = KEY_LEFT_CONTROL;	transition ? properties |= KP_CONTROL | KP_LEFT		 : properties -= (properties & KP_CONTROL) + (properties & KP_LEFT);		break;
		case WK_RCONTROL:				code = KEY_RIGHT_CONTROL;	transition ? properties |= KP_CONTROL | KP_RIGHT	 : properties -= (properties & KP_CONTROL) + (properties & KP_RIGHT);		break;
		case WK_LMENU:					code = KEY_LEFT_ALT;		transition ? properties |= KP_ALTERNATIVE | KP_LEFT	 : properties -= (properties & KP_ALTERNATIVE) + (properties & KP_LEFT);	break;
		case WK_RMENU:					code = KEY_RIGHT_ALT;		transition ? properties |= KP_ALTERNATIVE | KP_RIGHT : properties -= (properties & KP_ALTERNATIVE) + (properties & KP_RIGHT);	break;

		case WK_0:						code = KEY_0;	ch = ALTERNATIVE(CH_DIGIT_0, CH_RIGHT_PARENTHESIS,	CH_NULL);				break;
		case WK_1:						code = KEY_1;	ch = ALTERNATIVE(CH_DIGIT_1, CH_EXCLAMATION_MARK,	CH_SUPERSCRIPT_ONE);	break;
		case WK_2:						code = KEY_2;	ch = ALTERNATIVE(CH_DIGIT_2, CH_COMMERCIAL_AT, 		CH_SUPERSCRIPT_TWO);	break;
		case WK_3:						code = KEY_3;	ch = ALTERNATIVE(CH_DIGIT_3, CH_NUMBER_SIGN, 		CH_SUPERSCRIPT_THREE);	break;
		case WK_4:						code = KEY_4;	ch = ALTERNATIVE(CH_DIGIT_4, CH_DOLLAR_SIGN,		CH_POUND_SIGN);			break;
		case WK_5:						code = KEY_5;	ch = ALTERNATIVE(CH_DIGIT_5, CH_PERCENT_SIGN,		CH_CENT_SIGN);			break;
		case WK_6:						code = KEY_6;	ch = ALTERNATIVE(CH_DIGIT_6, CH_DIARESIS,			CH_NOT_SIGN);			break;
		case WK_7:						code = KEY_7;	ch = ALTERNATIVE(CH_DIGIT_7, CH_AMPERSAND,			CH_NULL);				break;
		case WK_8:						code = KEY_8;	ch = ALTERNATIVE(CH_DIGIT_8, CH_ASTERISK,			CH_NULL);				break;
		case WK_9:						code = KEY_9;	ch = ALTERNATIVE(CH_DIGIT_9, CH_LEFT_PARENTHESIS,	CH_NULL);				break;

		case WK_A:						code = KEY_A;	ch = CASE(CH_SMALL_LETTER_A, CH_CAPITAL_LETTER_A);		break;
		case WK_B:						code = KEY_B;	ch = CASE(CH_SMALL_LETTER_B, CH_CAPITAL_LETTER_B);		break;
		case WK_C:						code = KEY_C;	ch = CASE(CH_SMALL_LETTER_C, CH_CAPITAL_LETTER_C);		break;
		case WK_D:						code = KEY_D;	ch = CASE(CH_SMALL_LETTER_D, CH_CAPITAL_LETTER_D);		break;
		case WK_E:						code = KEY_E;	ch = CASE(CH_SMALL_LETTER_E, CH_CAPITAL_LETTER_E);		break;
		case WK_F:						code = KEY_F;	ch = CASE(CH_SMALL_LETTER_F, CH_CAPITAL_LETTER_F);		break;
		case WK_G:						code = KEY_G;	ch = CASE(CH_SMALL_LETTER_G, CH_CAPITAL_LETTER_G);		break;
		case WK_H:						code = KEY_H;	ch = CASE(CH_SMALL_LETTER_H, CH_CAPITAL_LETTER_H);		break;
		case WK_I:						code = KEY_I;	ch = CASE(CH_SMALL_LETTER_I, CH_CAPITAL_LETTER_I);		break;
		case WK_J:						code = KEY_J;	ch = CASE(CH_SMALL_LETTER_J, CH_CAPITAL_LETTER_J);		break;
		case WK_K:						code = KEY_K;	ch = CASE(CH_SMALL_LETTER_K, CH_CAPITAL_LETTER_K);		break;
		case WK_L:						code = KEY_L;	ch = CASE(CH_SMALL_LETTER_L, CH_CAPITAL_LETTER_L);		break;
		case WK_M:						code = KEY_M;	ch = CASE(CH_SMALL_LETTER_M, CH_CAPITAL_LETTER_M);		break;
		case WK_N:						code = KEY_N;	ch = CASE(CH_SMALL_LETTER_N, CH_CAPITAL_LETTER_N);		break;
		case WK_O:						code = KEY_O;	ch = CASE(CH_SMALL_LETTER_O, CH_CAPITAL_LETTER_O);		break;
		case WK_P:						code = KEY_P;	ch = CASE(CH_SMALL_LETTER_P, CH_CAPITAL_LETTER_P);		break;
		case WK_Q:						code = KEY_Q;	ch = CASE(CH_SMALL_LETTER_Q, CH_CAPITAL_LETTER_Q);		break;
		case WK_R:						code = KEY_R;	ch = CASE(CH_SMALL_LETTER_R, CH_CAPITAL_LETTER_R);		break;
		case WK_S:						code = KEY_S;	ch = CASE(CH_SMALL_LETTER_S, CH_CAPITAL_LETTER_S);		break;
		case WK_T:						code = KEY_T;	ch = CASE(CH_SMALL_LETTER_T, CH_CAPITAL_LETTER_T);		break;
		case WK_U:						code = KEY_U;	ch = CASE(CH_SMALL_LETTER_U, CH_CAPITAL_LETTER_U);		break;
		case WK_V:						code = KEY_V;	ch = CASE(CH_SMALL_LETTER_V, CH_CAPITAL_LETTER_V);		break;
		case WK_W:						code = KEY_W;	ch = CASE(CH_SMALL_LETTER_W, CH_CAPITAL_LETTER_W);		break;
		case WK_X:						code = KEY_X;	ch = CASE(CH_SMALL_LETTER_X, CH_CAPITAL_LETTER_X);		break;
		case WK_Y:						code = KEY_Y;	ch = CASE(CH_SMALL_LETTER_Y, CH_CAPITAL_LETTER_Y);		break;
		case WK_Z:						code = KEY_Z;	ch = CASE(CH_SMALL_LETTER_Z, CH_CAPITAL_LETTER_Z);		break;

		case WK_NUMPAD0:				code = KEY_NUMPAD_0;	ch = CH_DIGIT_0;				break;
		case WK_NUMPAD1:				code = KEY_NUMPAD_1;	ch = CH_DIGIT_1;				break;
		case WK_NUMPAD2:				code = KEY_NUMPAD_2;	ch = CH_DIGIT_2;				break;
		case WK_NUMPAD3:				code = KEY_NUMPAD_3;	ch = CH_DIGIT_3;				break;
		case WK_NUMPAD4:				code = KEY_NUMPAD_4;	ch = CH_DIGIT_4;				break;
		case WK_NUMPAD5:				code = KEY_NUMPAD_5;	ch = CH_DIGIT_5;				break;
		case WK_NUMPAD6:				code = KEY_NUMPAD_6;	ch = CH_DIGIT_6;				break;
		case WK_NUMPAD7:				code = KEY_NUMPAD_7;	ch = CH_DIGIT_7;				break;
		case WK_NUMPAD8:				code = KEY_NUMPAD_8;	ch = CH_DIGIT_8;				break;
		case WK_NUMPAD9:				code = KEY_NUMPAD_9;	ch = CH_DIGIT_9;				break;

		case WK_MULTIPLY:				code = KEY_MULTIPLY;	ch = CH_MULTIPLICATION_SIGN;	break;
		case WK_ADD:					code = KEY_ADD;			ch = CH_PLUS_SIGN;				break;
		case WK_SEPARATOR:				code = KEY_SEPARATOR;	ch = CH_FULL_STOP;				break;
		case WK_SUBTRACT:				code = KEY_SUB;			ch = CH_HYPHEN;					break;
		case WK_DECIMAL:				code = KEY_DECIMAL;		ch = CH_COMMA;					break;
		case WK_DIVIDE:					code = KEY_DIVIDE;		ch = CH_SOLIDUS;				break;

		case WK_F1:						code = KEY_F1;				break;
		case WK_F2:						code = KEY_F2;				break;
		case WK_F3:						code = KEY_F3;				break;
		case WK_F4:						code = KEY_F4;				break;
		case WK_F5:						code = KEY_F5;				break;
		case WK_F6:						code = KEY_F6;				break;
		case WK_F7:						code = KEY_F7;				break;
		case WK_F8:						code = KEY_F8;				break;
		case WK_F9:						code = KEY_F9;				break;
		case WK_F10:					code = KEY_F10;				break;
		case WK_F11:					code = KEY_F11;				break;
		case WK_F12:					code = KEY_F12;				break;
		case WK_F13:					code = KEY_F13;				break;
		case WK_F14:					code = KEY_F14;				break;
		case WK_F15:					code = KEY_F15;				break;
		case WK_F16:					code = KEY_F16;				break;
		case WK_F17:					code = KEY_F17;				break;
		case WK_F18:					code = KEY_F18;				break;
		case WK_F19:					code = KEY_F19;				break;
		case WK_F20:					code = KEY_F20;				break;
		case WK_F21:					code = KEY_F21;				break;
		case WK_F22:					code = KEY_F22;				break;
		case WK_F23:					code = KEY_F23;				break;
		case WK_F24:					code = KEY_F24;				break;

		case WK_OEM_1:					code = KEY_CEDILLA;			ch = CASE(CH_SMALL_LETTER_CEDILLA, CH_CAPITAL_LETTER_CEDILLA);	break;
		case WK_OEM_PLUS:				code = KEY_PLUS;			ch = CH_PLUS_SIGN;				break;
		case WK_OEM_COMMA:				code = KEY_COMMA;			ch = CH_COMMA;					break;
		case WK_OEM_MINUS:				code = KEY_MINUS;			ch = CH_HYPHEN;					break;
		case WK_OEM_PERIOD:				code = KEY_PERIOD;			ch = CH_FULL_STOP;				break;
		case WK_OEM_2:					code = KEY_COLON;			ch = ALTERNATIVE(CH_SEMICOLON,				CH_COLON,				CH_NULL);	break;
		case WK_OEM_3:					code = KEY_QUOTE;			ch = ALTERNATIVE(CH_APORTROPHE,				CH_QUOTATION_MARK,		CH_NULL);			break;
		case WK_OEM_4:					code = KEY_ACUTE;			ch = ALTERNATIVE(CH_ACUTE_ACCENT,			CH_GRAVE_ACCENT,		CH_NULL);			break;
		case WK_OEM_5:					code = KEY_RIGHT_BRACKET;	ch = ALTERNATIVE(CH_RIGHT_SQUARE_BRACKET,	CH_RIGHT_CURLY_BRACKET,	CH_NULL);			break;
		case WK_OEM_6:					code = KEY_LEFT_BRACKET;	ch = ALTERNATIVE(CH_LEFT_SQUARE_BRACKET,	CH_LEFT_CURLY_BRACKET,	CH_NULL);			break;
		case WK_OEM_7:					code = KEY_TILDE;			ch = ALTERNATIVE(CH_TILDE,					CH_CIRCUMFLEX_ACCENT,	CH_NULL);			break;
		case WK_OEM_9:					code = KEY_SLASH;			ch = ALTERNATIVE(CH_SOLIDUS,				CH_QUESTION_MARK,		CH_NUMBER_SIGN);	break;
		case WK_OEM_102:				code = KEY_BACK_SLASH;		ch = ALTERNATIVE(CH_REVERSE_SOLIDUS,		CH_VERTICAL_LINE,		CH_NUMBER_SIGN);	break;
	}

	if (type != KEY_TYPED)
		ch = CH_NULL;

	jclass keyEventClass = env->FindClass("org/diverproject/jni/input/KeyEvent");
	jmethodID constructor = env->GetMethodID(keyEventClass, "<init>", "(IIICI)V");
	jobject keyEvent = env->NewObject(keyEventClass, constructor, type, vk, code, ch, properties);

	return keyEvent;
}
