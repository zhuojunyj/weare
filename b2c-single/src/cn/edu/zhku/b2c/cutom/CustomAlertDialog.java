package cn.edu.zhku.b2c.cutom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import cn.edu.zhku.b2c.R;

public class CustomAlertDialog extends DialogFragment{
	private AlertDialog.Builder builder = null ;
	private AlertDialogListener dialogListener = null ;
	private Context context = null ;
	private Dialog dialog = null;
	public CustomAlertDialog(Context context){
		this.context = context ;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		builder = new AlertDialog.Builder(context) ;
		Activity activity = getActivity();
		if(activity instanceof AlertDialogListener){
			dialogListener = (AlertDialogListener) getActivity() ;
		}
		builder.setTitle(R.string.custom_alert_title) ;
		Bundle args = getArguments() ;
		builder.setMessage(args.getString("message")) ;
		builder.setPositiveButton(R.string.custom_alert_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
						if(dialogListener != null){
							dialogListener.onPostiveClick(CustomAlertDialog.this);
						}
					}
				});
		builder.setNegativeButton(R.string.custom_alert_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						if(dialogListener != null){
							dialogListener.onNegativeClick(CustomAlertDialog.this);
						}
					}
				});
		dialog = builder.create() ;
		return dialog ;
	}

	public interface AlertDialogListener{
		public void onPostiveClick(DialogFragment dialog) ;
		public void onNegativeClick(DialogFragment dialog) ;
	}
}
