package by.iba.bank.model.data;

/*
* Класс Rate содержит следующие свойства:

Cur_ID – внутренний код
Date – дата, на которую запрашивается курс
Cur_Abbreviation – буквенный код
Cur_Scale – количество единиц иностранной валюты
Cur_Name – наименование валюты на русском языке во множественном, либо в единственном числе, в зависимости от количества единиц
Cur_OfficialRate – курс**/

import lombok.Data;

@Data
public class Rate {
    int Cur_ID;
    String Date;
    String Cur_Abbreviation;
    double Cur_Scale;
    String Cur_Name;
    double Cur_OfficialRate;
}
