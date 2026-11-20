import json
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import Mascota

def a_dict(m):
    return {"id": m.id, "nombre": m.nombre, "especie": m.especie,
            "raza": m.raza, "edad": m.edad, "dueno": m.dueno}

@csrf_exempt
def mascotas(request):
    if request.method == "GET":
        return JsonResponse([a_dict(m) for m in Mascota.objects.all()], safe=False)
    if request.method == "POST":
        data = json.loads(request.body)
        m = Mascota.objects.create(**data)
        return JsonResponse(a_dict(m), status=201)

@csrf_exempt
def mascota_detalle(request, id):
    try:
        m = Mascota.objects.get(id=id)
    except Mascota.DoesNotExist:
        return JsonResponse({"error": "No encontrada"}, status=404)

    if request.method == "GET":
        return JsonResponse(a_dict(m))
    if request.method == "PUT":
        data = json.loads(request.body)
        for campo, valor in data.items():
            setattr(m, campo, valor)
        m.save()
        return JsonResponse(a_dict(m))
    if request.method == "DELETE":
        m.delete()
        return JsonResponse({"mensaje": "Eliminada"})