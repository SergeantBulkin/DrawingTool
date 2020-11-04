package by.sergeantbulkin.drawingtool;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import by.sergeantbulkin.drawingtool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    private static final int PERMISSION_CODE = 1000;
    private static final int GALLERY_REQUEST = 1002;
    ActivityMainBinding binding;
    //Диалог выбора цвета
    ColorPickerDialog colorPickerDialog;
    //Дилог выбора формы
    BottomSheetDialog shapeDialog;
    //Диалог для выбора варианта загрузки
    BottomSheetDialog uploadDialog;
    //Clicked MenuItem для изменения иконки
    MenuItem clickedItem;
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUp();
    }
    //----------------------------------------------------------------------------------------------
    private void setUp()
    {
        //Установка BottomBar
        setSupportActionBar(binding.bottomAppBar);

        //Обработка нажатия на NavigationIcon
        binding.bottomAppBar.setNavigationOnClickListener(v ->
        {

        });

        //Настройка ColorPickerDialog
        colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this);
        colorPickerDialog.hideColorComponentsInfo();
        colorPickerDialog.hideHexaDecimalValue();
        colorPickerDialog.setOnColorPickedListener((color, hexVal) ->
        {
            //Установить цвет рисовалки
            binding.drawingView.setDrawPaintColor(color);
        });

        //Подготовка BottomSheet для выбора формы рисования
        setUpShapeSheet();

        //Подготовка BottomSheet для выбора варианта загрузки
        setUpUploadingSheet();

        //Запросить разрешения
        requestPermissions();

        //Передать высоту бара DrawingView
        binding.drawingView.setBottomBarHeight(binding.bottomAppBar.getMinimumHeight());
    }
    //----------------------------------------------------------------------------------------------
    //Установить Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_bar_menu, menu);
        return true;
    }
    //----------------------------------------------------------------------------------------------
    //Обработка нажатия на элементы Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        clickedItem = item;
        if (id == R.id.drawShape)
        {
            shapeDialog.show();
            return true;
        } else if (id == R.id.colorPicker)
        {
            colorPickerDialog.show();
            return true;
        } else if (id == R.id.uploadImage)
        {
            uploadDialog.show();
            return true;
        } else if (id == R.id.clear)
        {
            binding.drawingView.clearAll();
        } else if (id == R.id.more)
        {
            return true;
        } else if (id == R.id.save)
        {
            //Сохранить картинку
            saveImage();
        }
        return false;
    }
    //----------------------------------------------------------------------------------------------
    //Подготовка BottomSheet для выбора формы рисования
    private void setUpShapeSheet()
    {
        //Создать диалог для выбора формы
        shapeDialog = new BottomSheetDialog(this);
        shapeDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_shape, null));
        //Объявление выбора форм рисования и установка слушателей нажатия
        LinearLayout linear_layout_curved_line = shapeDialog.findViewById(R.id.linear_layout_curved_line);
        linear_layout_curved_line.setOnClickListener(v ->
        {
            //Установить выбранную форму
            binding.drawingView.setDrawShape(1);
            //Изменить иконку
            clickedItem.setIcon(R.drawable.ic_pen);
            //Убрать диалог
            shapeDialog.dismiss();
        });

        LinearLayout linear_layout_line = shapeDialog.findViewById(R.id.linear_layout_line);
        linear_layout_line.setOnClickListener(v ->
        {
            binding.drawingView.setDrawShape(2);
            clickedItem.setIcon(R.drawable.ic_diagonal_line);
            shapeDialog.dismiss();
        });

        LinearLayout linear_layout_rectangle = shapeDialog.findViewById(R.id.linear_layout_rectangle);
        linear_layout_rectangle.setOnClickListener(v ->
        {
            binding.drawingView.setDrawShape(3);
            clickedItem.setIcon(R.drawable.ic_rectangle);
            shapeDialog.dismiss();
        });

        LinearLayout linear_layout_square = shapeDialog.findViewById(R.id.linear_layout_square);
        linear_layout_square.setOnClickListener(v ->
        {
            binding.drawingView.setDrawShape(5);
            clickedItem.setIcon(R.drawable.ic_square);
            shapeDialog.dismiss();
        });

        LinearLayout linear_layout_circle = shapeDialog.findViewById(R.id.linear_layout_circle);
        linear_layout_circle.setOnClickListener(v ->
        {
            binding.drawingView.setDrawShape(6);
            clickedItem.setIcon(R.drawable.ic_circle);
            shapeDialog.dismiss();
        });

        LinearLayout linear_layout_triangle = shapeDialog.findViewById(R.id.linear_layout_triangle);
        linear_layout_triangle.setOnClickListener(v ->
        {
            binding.drawingView.setDrawShape(7);
            clickedItem.setIcon(R.drawable.ic_triangle);
            shapeDialog.dismiss();
        });
    }
    //----------------------------------------------------------------------------------------------
    //Подготовка BottomSheet для выбора варианта загрузки
    private void setUpUploadingSheet()
    {
        uploadDialog = new BottomSheetDialog(this);
        uploadDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_upload, null));
        //Объявление выбора загрузки картинки и установка слушателей нажатия
        LinearLayout linear_layout_camera = uploadDialog.findViewById(R.id.linear_layout_camera);
        linear_layout_camera.setOnClickListener(v ->
        {
            //Закрыть диалог
            uploadDialog.dismiss();

            //Из камеры
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(binding.drawingView.getWidthView(), binding.drawingView.getHeightView())
                    .setFixAspectRatio(true)
                    .start(this);

        });

        LinearLayout linear_layout_gallery = uploadDialog.findViewById(R.id.linear_layout_gallery);
        linear_layout_gallery.setOnClickListener(v ->
        {
            //Закрыть диалог
            uploadDialog.dismiss();

            //Запустить галерею
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });
    }
    //----------------------------------------------------------------------------------------------
    //Сохранить картинку
    private void saveImage()
    {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Filename.jpg");
        try(FileOutputStream outputStream = new FileOutputStream(file))
        {
            Bitmap bitmap = binding.drawingView.getCanvasBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Snackbar.make(binding.getRoot(), "Saved", BaseTransientBottomBar.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            addToLog("Exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------------------
    //Запрос разрешений
    private void requestPermissions()
    {
        //Проверить разрешения камеры и записи в хранилище
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            //Инициализация массива строк с необходимыми разрешениями
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //Запросить разрешение
            requestPermissions(permissions, PERMISSION_CODE);
        }
    }
    //----------------------------------------------------------------------------------------------
    //Обработка полученного ответа для разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)
            {
                addToLog("Разрешения получены");
            } else
            {
                Snackbar.make(binding
                        .getRoot(), "Разрешеня не получены", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == GALLERY_REQUEST)
            {
                if (data.getData() != null)
                {
                    Uri selectedImage = data.getData();
                    //Вызвать окно редактирования картинки
                    CropImage.activity(selectedImage)
                            .setAspectRatio(binding.drawingView.getWidthView(), binding.drawingView.getHeightView())
                            .setFixAspectRatio(true)
                            .start(this);
                } else
                {
                    //Показать сообщение об ошибке
                    Snackbar.make(binding.getRoot(), "Ошибка загрузки", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                try
                {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    binding.drawingView.setBitmap(scaleBitmap(bitmap));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    //Изменить размер картинки
    private Bitmap scaleBitmap(Bitmap bitmap)
    {
        int srcW = bitmap.getWidth();
        int srcH = bitmap.getHeight();
        int viewH = binding.drawingView.getHeightView();
        int viewW = binding.drawingView.getWidthView();
        int dstW = srcW;
        int dstH = srcH;
        addToLog("old W - " + srcW + "| H - " + srcH + "|");

        //Пока ширина или высота картинки больше чем DrawingView
        if (dstW > viewW || dstH > viewH)
        {
            if (srcH == srcW) //Квадратная картинка
            {
                addToLog("Квадратная картинка");
                dstW = (srcH*viewH)/srcH;
                dstH = dstW;
                addToLog("new W - " + dstW + "| H - " + dstW + "|");
                bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstW, true);

            } else //Вертикальная или горизонтальная картинка
            {
                addToLog("Вертикальная картинка");

                //Исправить высоту
                if (dstH > viewH)
                {
                    dstH = viewH;
                    dstW = (srcW*dstH)/srcH;
                    addToLog("Высоту W - " + dstW + "| H - " + dstH + "|");
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
                //Исправить ширину
                if (dstW > viewW)
                {
                    dstW = viewW;
                    dstH = (dstW*srcH)/srcW;
                    addToLog("Ширину W - " + dstW + "| H - " + dstH + "|");
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
            }
        }

        //Если ширина или высота картинки меньше чем DrawingView
        if (dstW < viewW || dstH < viewH)
        {
            if (srcH == srcW) //Квадратная картинка
            {
                addToLog("Квадратная картинка");
                dstW = (srcH*viewH)/srcH;
                addToLog("new W - " + dstW + "| H - " + dstW + "|");
                bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstW, true);

            } else //Вертикальная или горизонтальная картинка
            {
                addToLog("Вертикальная картинка");

                //Исправить высоту
                if (dstH < viewH)
                {
                    dstH = viewH;
                    dstW = (srcW*dstH)/srcH;
                    addToLog("Высоту W - " + dstW + "| H - " + dstH + "|");
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
                //Исправить ширину
                if (dstW < viewW)
                {
                    dstW = viewW;
                    dstH = (dstW*srcH)/srcW;
                    addToLog("Ширину W - " + dstW + "| H - " + dstH + "|");
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
            }
        }

        return bitmap;
    }
    //----------------------------------------------------------------------------------------------
    private void addToLog(String msg)
    {
        Log.d("TAG", msg);
    }
    //----------------------------------------------------------------------------------------------
}