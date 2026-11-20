from django.urls import path
from . import views

urlpatterns = [
    path('mascotas/', views.mascotas),
    path('mascotas/<int:id>/', views.mascota_detalle),
]