# Predicts diseases based on the symptoms entered and selected by the user.
# importing all necessary libraries
import warnings
import nltk
import numpy as np
import pandas as pd
from sklearn.metrics import accuracy_score, precision_recall_fscore_support
from sklearn.model_selection import train_test_split, cross_val_score
from statistics import mean
from nltk.corpus import wordnet 
import requests
from bs4 import BeautifulSoup
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from nltk.tokenize import RegexpTokenizer
from itertools import combinations
from time import time
from collections import Counter
import operator
import math
from Treatment import diseaseDetail
from sklearn.linear_model import LogisticRegression
import streamlit as st
from streamlit_chat import message
import pickle




# returns the list of synonyms of the input word from thesaurus.com (https://www.thesaurus.com/) and wordnet (https://www.nltk.org/howto/wordnet.html)
def synonyms(term):
    synonyms = []
    response = requests.get('https://www.thesaurus.com/browse/{}'.format(term))
    soup = BeautifulSoup(response.content,  "html.parser")
    try:
        container=soup.find('section', {'class': 'MainContentContainer'}) 
        row=container.find('div',{'class':'css-191l5o0-ClassicContentCard'})
        row = row.find_all('li')
        for x in row:
            synonyms.append(x.get_text())
    except:
        None
    for syn in wordnet.synsets(term):
        synonyms+=syn.lemma_names()
    return set(synonyms)

# utlities for pre-processing
stop_words = stopwords.words('english')
lemmatizer = WordNetLemmatizer()
splitter = RegexpTokenizer(r'\w+')


# Load Dataset scraped from NHP (https://www.nhp.gov.in/disease-a-z) & Wikipedia
# Scrapping and creation of dataset csv is done in a separate program
df_comb = pd.read_csv("C:\\Users\\USER\\Downloads\\chatbot\\Disease-Detection-based-on-Symptoms\\Dataset\\dis_sym_dataset_comb.csv") # Disease combination
df_norm = pd.read_csv("C:\\Users\\USER\\Downloads\\chatbot\\Disease-Detection-based-on-Symptoms\\Dataset\\dis_sym_dataset_norm.csv") # Individual Disease

X = df_comb.iloc[:, 1:]
Y = df_comb.iloc[:, 0:1]



model = pickle.load(open('C:\\Users\\USER\\Downloads\\chatbot\\model.pkl','rb'))
scores = cross_val_score(model, X, Y,cv=5)

# List of symptoms
dataset_symptoms = list(X.columns)



# Taking symptoms from user as input 
message("Hey User! Please enter symptoms separated by comma(,):\n")
user_symptoms = st.text_input("you:").lower().split(',')
if len(user_symptoms)!=0:
# Preprocessing the input symptoms
    processed_user_symptoms=[]
    for sym in user_symptoms:
        sym=sym.strip()
        sym=sym.replace('-',' ')
        sym=sym.replace("'",'')
        sym = ' '.join([lemmatizer.lemmatize(word) for word in splitter.tokenize(sym)])
        processed_user_symptoms.append(sym)


    # Taking each user symptom and finding all its synonyms and appending it to the pre-processed symptom string
    user_symptoms = []
    for user_sym in processed_user_symptoms:
        user_sym = user_sym.split()
        str_sym = set()
        for comb in range(1, len(user_sym)+1):
            for subset in combinations(user_sym, comb):
                subset=' '.join(subset)
                subset = synonyms(subset) 
                str_sym.update(subset)
        str_sym.add(' '.join(user_sym))
        user_symptoms.append(' '.join(str_sym).replace('_',' '))
    # query expansion performed by joining synonyms found for each symptoms initially entered
    #print("After query expansion done by using the symptoms entered")
    #print(user_symptoms)

   

    # Loop over all the symptoms in dataset and check its similarity score to the synonym string of the user-input 
    # symptoms. If similarity>0.5, add the symptom to the final list
    found_symptoms = set()
    for idx, data_sym in enumerate(dataset_symptoms):
        data_sym_split=data_sym.split()
        for user_sym in user_symptoms:
            count=0
            for symp in data_sym_split:
                if symp in user_sym.split():
                    count+=1
            if count/len(data_sym_split)>0.5:
                found_symptoms.add(data_sym)
    found_symptoms = list(found_symptoms)
    s1 = ""
    for idx, symp in enumerate(found_symptoms):
        s1+=str(idx)+" "+":"+symp+"\n"
    

    # Print all found symptoms
    
    message("Top matching symptoms from your search!\n"+ s1 + "\nPlease select the relevant symptoms. Enter indices (separated-space):\n")    
    # Show the related symptoms found in the dataset and ask user to select among them
    select_list = st.text_input("you1:")
    select_list = select_list.split()
    if len(select_list)!=0:
        # Find other relevant symptoms from the dataset based on user symptoms based on the highest co-occurance with the
        # ones that is input by the user
        dis_list = set()
        final_symp = [] 
        counter_list = []
        for idx in select_list:
            symp=found_symptoms[int(idx)]
            final_symp.append(symp)
            dis_list.update(set(df_norm[df_norm[symp]==1]['label_dis']))
        
        for dis in dis_list:
            row = df_norm.loc[df_norm['label_dis'] == dis].values.tolist()
            row[0].pop(0)
            for idx,val in enumerate(row[0]):
                if val!=0 and dataset_symptoms[idx] not in final_symp:
                    counter_list.append(dataset_symptoms[idx])

       

        # Symptoms that co-occur with the ones selected by user              
        dict_symp = dict(Counter(counter_list))
        dict_symp_tup = sorted(dict_symp.items(), key=operator.itemgetter(1),reverse=True)   
        #print(dict_symp_tup)

        

        # Iteratively, suggest top co-occuring symptoms to the user and ask to select the ones applicable 
        found_symptoms=[]
        count=0
        flag=0
        i=123
        w = ""
        if(len(dict_symp_tup)<15):
            for tup in dict_symp_tup:
                found_symptoms.append(tup[0])
        else:
            for tup in dict_symp_tup[:14]:
                found_symptoms.append(tup[0])
        for idx,ele in enumerate(found_symptoms):
                w+=(str(idx)+":"+ele+"\n")

            
        message("\nCommon co-occuring symptoms:\n"+w+"\n"+"Do you have any of these symptoms:\n")
        
        s=st.text_input("u:").lower().split()


        

        if len(s)!=0:
            # Create query vector based on symptoms selected by the user
            #message("\nFinal list of Symptoms that will be used for prediction:")
            for idx in s:
                final_symp.append(found_symptoms[int(idx)])

            sample_x = [0 for x in range(0,len(dataset_symptoms))]
            for val in final_symp:
                #print(val)
                sample_x[dataset_symptoms.index(val)]=1

           

            # Predict disease
            prediction = model.predict([sample_x])

            

            k = 5
            diseases = list(set(Y['label_dis']))
            diseases.sort()
            topk = np.array(prediction[0]).argsort()[-k:][::-1]

            

            #message(f"\nTop {k} diseases predicted based on symptoms")
            td = ""
            topk_dict = {}
            # Show top 10 highly probable disease to the user.
            for idx,t in  enumerate(topk):
                match_sym=set()
                row = df_norm.loc[df_norm['label_dis'] == diseases[t]].values.tolist()
                row[0].pop(0)

                for idx,val in enumerate(row[0]):
                    if val!=0:
                        match_sym.add(dataset_symptoms[idx])
                prob = (len(match_sym.intersection(set(final_symp)))+1)/(len(set(final_symp))+1)
                prob *= mean(scores)
                topk_dict[t] = prob
            j = 0
            topk_index_mapping = {}
            topk_sorted = dict(sorted(topk_dict.items(), key=lambda kv: kv[1], reverse=True))
            print(topk_sorted)
            for key in topk_sorted:
                prob = topk_sorted[key]*100
                print(diseases[key])
                td+=(str(j) + " Disease name:"+str(diseases[key])+ "\tProbability:"+str(round(prob, 2))+"%"+"\n")
                topk_index_mapping[j] = key
                j += 1
            message("\nTop diseases predicted based on symptoms\n"+td+"\n")
           
