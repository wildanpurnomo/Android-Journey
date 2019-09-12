using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FuelFreeSPBUSimulator.model
{
    internal class QuickCastNota
    {
        public string token, productBought, nominalBought, dateOfTransaction, literGet;

        public QuickCastNota()
        {

        }

        public QuickCastNota(string token, string productBought, string nominalBought, string dateOfTransaction, string literGet)
        {
            this.token = token;
            this.productBought = productBought;
            this.nominalBought = nominalBought;
            this.dateOfTransaction = dateOfTransaction;
            this.literGet = literGet;
        }
    }
}
