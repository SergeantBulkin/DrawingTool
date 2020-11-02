package by.sergeantbulkin.drawingtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import by.sergeantbulkin.drawingtool.databinding.ActivityMainBinding;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

public class MainActivity extends AppCompatActivity
{
    ActivityMainBinding binding;
    ColorPickerDialog colorPickerDialog;
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
        if (id == R.id.colorPicker)
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