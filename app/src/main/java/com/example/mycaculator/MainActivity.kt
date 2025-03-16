package com.example.mycaculator
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var txt_pt : TextView
    private lateinit var txt_kq : TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Ánh xạ textView
        txt_pt = findViewById(R.id.txt_pt);
        txt_kq = findViewById(R.id.txt_kq);
        // Ánh xạ button
        val btn_0 = findViewById<Button>(R.id.btn_0);
        val btn_1 = findViewById<Button>(R.id.btn_1);
        val btn_2 = findViewById<Button>(R.id.btn_2);
        val btn_3 = findViewById<Button>(R.id.btn_3);
        val btn_4 = findViewById<Button>(R.id.btn_4);
        val btn_5 = findViewById<Button>(R.id.btn_5);
        val btn_6 = findViewById<Button>(R.id.btn_6);
        val btn_7 = findViewById<Button>(R.id.btn_7);
        val btn_8 = findViewById<Button>(R.id.btn_8);
        val btn_9 = findViewById<Button>(R.id.btn_9);
        val btn_cong = findViewById<Button>(R.id.btn_cong);
        val btn_tru = findViewById<Button>(R.id.btn_tru);
        val btn_nhan = findViewById<Button>(R.id.btn_nhan);
        val btn_chia = findViewById<Button>(R.id.btn_chia);
        val btn_bs = findViewById<Button>(R.id.btn_bs);
        val btn_c = findViewById<Button>(R.id.btn_c);
        val btn_ce= findViewById<Button>(R.id.btn_ce);
        val btn_doidau = findViewById<Button>(R.id.btn_doidau);
        val btn_bang = findViewById<Button>(R.id.btn_bang);
        // Xu li su kien nhan so
        btn_0.setOnClickListener {writeNumberInCalculation("0")};
        btn_1.setOnClickListener {writeNumberInCalculation("1")};
        btn_2.setOnClickListener {writeNumberInCalculation("2")};
        btn_3.setOnClickListener {writeNumberInCalculation("3")};
        btn_4.setOnClickListener {writeNumberInCalculation("4")};
        btn_5.setOnClickListener {writeNumberInCalculation("5")};
        btn_6.setOnClickListener {writeNumberInCalculation("6")};
        btn_7.setOnClickListener {writeNumberInCalculation("7")};
        btn_8.setOnClickListener {writeNumberInCalculation("8")};
        btn_9.setOnClickListener {writeNumberInCalculation("9")};
        // Xu li su kien nhan phep tinh
        btn_cong.setOnClickListener { writeOperatorInCalculation("+") };
        btn_tru.setOnClickListener { writeOperatorInCalculation("-") };
        btn_nhan.setOnClickListener { writeOperatorInCalculation("*") };
        btn_chia.setOnClickListener { writeOperatorInCalculation("/") };
        // Xu li su kien nhan phim doi dau
        btn_doidau.setOnClickListener {
            val text = txt_pt.text.toString();
            if (text.isNotEmpty()) {
                val regex = Regex("[-+]?[0-9]+$");
                val match = regex.find(text);
                if (match!=null) {
                    val lastNumber = match.value;
                    val oppositeNumber: String;
                    oppositeNumber = if (lastNumber[0] == '-') lastNumber.drop(1); else "-$lastNumber";
                    txt_pt.text = text.replaceRange(match.range, oppositeNumber);
                }
            }
        }
        // Xu li su kien backspace
        btn_bs.setOnClickListener {
            val text = txt_pt.text.toString();
            if(text.isNotEmpty()) txt_pt.text = text.dropLast(1);
        }
        // Xu li su kien nhan phim C
        btn_c.setOnClickListener {
            txt_pt.text = "";
            txt_kq.text = "";
        }
        // Xu li su kien nhan phim CE
        btn_ce.setOnClickListener {
            val text = txt_pt.text.toString();
            val regex = Regex("[-+]?[0-9]+$");
            val match = regex.find(text);
            if (match!=null) txt_pt.text = text.replaceRange(match.range, "");
        }
        // Xác định độ ưu tiên của toán tử
        fun precedence(op: Char) = when (op) {
            '+', '-' -> 1;
            '*', '/' -> 2;
            else -> -1;
        }
        // Thực hiện phép toán giữa hai số
        fun applyOp(op: Char, a: Int, b: Int) = when (op) {
            '+' -> a + b;
            '-' -> a - b;
            '*' -> a * b;
            '/' -> a / b;
            else -> throw IllegalArgumentException("Opperator isn't available");
        }
        // Hàm tính toán biểu thức đầu vào
        fun calculateExpression(expression: String): Int {
            val tokens = expression.split("(?=[-+*/])|(?<=[-+*/])".toRegex());  // Tach toán tử và toán hạng
            val numbers = mutableListOf<Int>();
            val operators = mutableListOf<Char>();
            var i = 0;
            while (i < tokens.size) {
                val token = tokens[i];
                when {
                    token.matches(Regex("-?\\d+")) -> numbers.add(token.toInt());
                    "+-*/".contains(token) -> {
                        while (operators.isNotEmpty() && precedence(operators.last()) >= precedence(token[0])) {
                            val b = numbers.removeAt(numbers.lastIndex);
                            val a = numbers.removeAt(numbers.lastIndex);
                            numbers.add(applyOp(operators.removeAt(operators.lastIndex), a, b));
                        }
                        operators.add(token[0]);
                    }
                }
                i++;
            }

            while (operators.isNotEmpty()) {
                val b = numbers.removeAt(numbers.lastIndex);
                val a = numbers.removeAt(numbers.lastIndex);
                numbers.add(applyOp(operators.removeAt(operators.lastIndex), a, b));
            }
            return numbers[0];
        }
        // Xu li su kien nhan phim =
        btn_bang.setOnClickListener {
            val expression = txt_pt.text.toString();
            if (expression.isEmpty()) return@setOnClickListener;

            try {
                val result = calculateExpression(expression);
                txt_kq.text = result.toString();
            } catch (e: Exception) {
                txt_kq.text = "Error!";
            }
        }
    }

    private fun writeNumberInCalculation(number : String) {
        var text = txt_pt.text.toString();
        if (text.isEmpty()) text = number; else text += number;
        txt_pt.text = text;
        return;
    }

    @SuppressLint("SetTextI18n")
    private fun writeOperatorInCalculation(operator : String) {
        val text = txt_pt.text.toString();
        if (text.isEmpty()) return;
        if ("+-*/".contains(text.last())) txt_pt.text = text.dropLast(1) + operator;
        else txt_pt.text = text + operator;
    }
}