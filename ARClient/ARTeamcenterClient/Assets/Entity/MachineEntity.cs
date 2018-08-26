using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Assets.Entity
{
    [Serializable]
    class MachineEntity
    {
        public string id;
        public string xAxis;
        public string yAxis;
        public string fileName;
        public string revisionId;
        public string errorState;
        public string machineCADFile;
        public string operationToDo;
        public List<string> operationInstructionList;
        public string operationInstructionListId;
    }
}
