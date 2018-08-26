using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Assets.Entity
{
    [Serializable]
    class UpdateInstructionsEntity
    {
        public string operationId;
        public string operationInstructionListId;
        public List<string> operationInstructionList;
        public bool changeListType;
    }
}