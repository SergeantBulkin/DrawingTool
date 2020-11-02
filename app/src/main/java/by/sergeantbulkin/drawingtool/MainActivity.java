package by.sergeantbulkin.drawingtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import by.sergeantbulkin.drawingtool.databinding.ActivityMainBinding;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity
{
    ActivityMainBinding binding;
    //Диалог выбора цвета
    ColorPickerDialog colorPickerDialog;
    //Дилог выбора формы
    BottomSheetDialog shapeDialog;
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

        //Создать диалог для выбора формы
        shapeDialog = new BottomSheetDialog(this);
        shapeDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_shape, null));
        //Объявление строк выбора форм рисования и установка слушателей нажатия
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
    //Установить Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_bar_menu, menu);
        return true;
    }

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
            return true;
        } else if (id == R.id.more)
        {
            return true;
        }
        return false;
    }
    //----------------------------------------------------------------------------------------------
    private void addToLog(String msg)
    {
        Log.d("TAG", msg);
    }
}