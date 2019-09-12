using Firebase.Database;
using Firebase.Database.Query;
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
using System.Windows.Shapes;

namespace FuelFreeSPBUSimulator
{
    /// <summary>
    /// Interaction logic for FillingMachineSimulator.xaml
    /// </summary>
    public partial class FillingMachineSimulator : Window
    {
        private int nominal;

        public string prodName;
        public string nominalBought;


        public FillingMachineSimulator(string prodName, string nominalBought)
        {
            InitializeComponent();
            this.prodName = prodName;
            this.nominalBought = nominalBought;

            this.KeyDown += new KeyEventHandler(Window_KeyDown);
            btnBackAndDestroyToken.IsEnabled = false;

            Nominal.Content = nominalBought;
            ProductName.Content = prodName;

            Console.WriteLine(nominalBought + prodName);
        }

        private void Window_KeyDown(object sender, KeyEventArgs e)
        {
            nominal = Int32.Parse(Nominal.Content.ToString());

            if (e.Key == Key.Space)
            {
                if(nominal == 0)
                {
                    MessageBox.Show("Pengisian selesai. Terima kasih telah menggunakan Fuel Free.", "Pengisian Selesai");
                    btnBackAndDestroyToken.IsEnabled = true;

                    return;

                }

                nominal -= 50;
                Nominal.Content = nominal.ToString();

            }
        }

        private async Task searchForNotaAsync(string token)
        {
            var Firebase = new FirebaseClient("https://project-fuel-free.firebaseio.com/");
            var quickNota = await Firebase.Child("Quick Cast Nota").OnceAsync<model.QuickCastNota>();

            
        }

        private static async Task firebaseMethodAsync()
        {
            var firebase = new FirebaseClient("https://project-fuel-free.firebaseio.com/");

            string tokenDeleted = ((MainWindow)Application.Current.MainWindow).tokenSearched.ToString();

            await firebase.Child("Quick Cast Nota").Child(tokenDeleted).DeleteAsync();
        }

        private void btnBackAndDestroyToken_Click(object sender, RoutedEventArgs e)
        {
            firebaseMethodAsync();
            Close();
        }
    }
}
