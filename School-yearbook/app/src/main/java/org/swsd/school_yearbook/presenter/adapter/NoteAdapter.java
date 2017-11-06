package org.swsd.school_yearbook.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.swsd.school_yearbook.R;
import org.swsd.school_yearbook.model.bean.SchoolyearbookBean;
import org.swsd.school_yearbook.view.activity.NewPersonActivity;

import java.util.List;

/**
 * author     :  骆景钊
 * time       :  2017/11/04
 * description:  记录Recyclerview的每一个List的适配器
 * version:   :  1.0
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>
        implements  View.OnLongClickListener{

    private List<SchoolyearbookBean>mSchoolyearbookList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View noteView;
        ImageView photoView;
        TextView noteName;
        TextView noteAddress;
        TextView notePhone;
        TextView noteWechat;
        TextView noteEmail;
        TextView noteQQ;
        TextView noteSignature;
        public ViewHolder(View view){
            super(view);
            noteView = view;
            noteName = view.findViewById(R.id.tv_note_name);
            noteAddress = view.findViewById(R.id.tv_note_address);
            notePhone = view.findViewById(R.id.tv_note_phone);
            noteWechat = view.findViewById(R.id.tv_note_wechat);
            noteEmail = view.findViewById(R.id.tv_note_email);
            noteQQ = view.findViewById(R.id.tv_note_qq);
            noteSignature = view.findViewById(R.id.tv_note_signature);
            photoView=view.findViewById(R.id.iv_note_header);
        }
    }

    public NoteAdapter(Context context,List<SchoolyearbookBean>schoolyearbooks){
        mContext = context;
        mSchoolyearbookList=schoolyearbooks;
    }

    public NoteAdapter(Context context){
        mContext = context;
        mSchoolyearbookList = DataSupport.findAll(SchoolyearbookBean.class);
    }


    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public interface OnItemOnClickListener{
        void onItemLongOnClick(View view ,int pos);
    }

    private OnItemOnClickListener mOnItemOnClickListener;
    public void setOnItemClickListener(OnItemOnClickListener listener){
        this.mOnItemOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        //点击主页跳转到编辑页
        holder.noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                SchoolyearbookBean book = mSchoolyearbookList.get(position);

                int id = book.getId();
                String name = book.getName();
                String address = book.getAddress();
                String phone = book.getPhone();
                String wechat = book.getWechat();
                String email = book.getEmail();
                String qq = book.getQq();
                String signature = book.getSignature();
                String AvatarPath=book.getAvatarPath();

                SchoolyearbookBean newSchoolyearbook
                        = new SchoolyearbookBean(id,name,address,phone,wechat,email,qq,signature,AvatarPath);

                Intent intent = new Intent(mContext, NewPersonActivity.class);

                Bundle bundle = new Bundle();
                //通过bundle传输数据
                bundle.putSerializable("note",newSchoolyearbook);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final NoteAdapter.ViewHolder holder, final int position) {
        SchoolyearbookBean schoolyearbookBean = mSchoolyearbookList.get(position);
        holder.noteName.setText(schoolyearbookBean.getName());
        holder.noteAddress.setText(schoolyearbookBean.getAddress());
        holder.notePhone.setText(schoolyearbookBean.getPhone());
        holder.noteWechat.setText(schoolyearbookBean.getWechat());
        holder.noteEmail.setText(schoolyearbookBean.getEmail());
        holder.noteQQ.setText(schoolyearbookBean.getQq());
        holder.noteSignature.setText(schoolyearbookBean.getSignature());

        //加载头像
        String imagePath=schoolyearbookBean.getAvatarPath();
        if(imagePath!=null){
            BitmapFactory.Options options = new BitmapFactory.Options();//解析位图的附加条件
            options.inJustDecodeBounds = true;// 不去解析位图，只获取位图头文件信息
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath,options);
            holder.photoView.setImageBitmap(bitmap);
            int btwidth = options.outWidth;//获取图片的宽度
            int btheight = options.outHeight;//获取图片的高度

            int dx = btwidth/200;//获取水平方向的缩放比例
            int dy = btheight/200;//获取垂直方向的缩放比例

            int s = 1;//设置默认缩放比例

            //如果是水平方向
            if (dx>dy&&dy>1) {
                s = dx;
            }

            //如果是垂直方向
            if (dy>dx&&dx>1) {
                s = dy;
            }
            options.inSampleSize = s;//设置图片缩放比例
            options.inJustDecodeBounds = false;//真正解析位图
            //把图片的解析条件options在创建的时候带上
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            holder.photoView.setImageBitmap(bitmap);//设置图片
        }else{
            holder.photoView.setImageResource(R.drawable.filemiss);
        }

        if(mOnItemOnClickListener!=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemOnClickListener.onItemLongOnClick(holder.itemView,position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mSchoolyearbookList.size();
    }


    public List<SchoolyearbookBean> getmSchoolyearbookList(){
        return  mSchoolyearbookList;
    }

}
