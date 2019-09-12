using Firebase.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace FuelFreeSPBUSimulator
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private FillingMachineSimulator fillingMachineWindow;


        //for new window
        public string tokenSearched = "";
        public string nominalBought = "";
        public string productBought = "";

        public MainWindow()
        {
            InitializeComponent();
            
        }

        private void tokenTextBox_GotFocus(object sender, RoutedEventArgs e)
        {
            TextBox tb = (TextBox)sender;
            tb.Text = string.Empty;
            tb.GotFocus -= tokenTextBox_GotFocus;
        }

        private void btnSearchToken_Click(object sender, RoutedEventArgs e)
        {
            string token = tokenTextBox.Text.ToString();
            searchForNotaAsync(token);
            
        }

        private async Task searchForNotaAsync(string token)
        {
            var Firebase = new FirebaseClient("https://project-fuel-free.firebaseio.com/");
            var quickNota = await Firebase.Child("Quick Cast Nota").OnceAsync<model.QuickCastNota>();

            foreach (var nota in quickNota)
            {
                if (nota.Key == token)
                {
                    tokenSearched = nota.Key;
                    fillingMachineWindow = new FillingMachineSimulator(nota.Object.productBought, nota.Object.nominalBought);
                }
            }

            if(tokenSearched != "")
            {
                MessageBox.Show("Token ditemukan, redirecting...", "Hasil Pencarian");
                fillingMachineWindow.Show();
                tokenSearched = "";
            }

            else
            {
                MessageBox.Show("Maaf, token tidak tersedia", "Hasil Pencarian");
            }
        }
    }
}
