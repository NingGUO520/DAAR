from django.conf.urls import include, url
from django.contrib import admin

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^polls/', include('polls.urls')),
    url(r'^snippets/', include('snippets.urls')),
    url(r'^pollsAPI/', include('pollsAPI.urls')),
]