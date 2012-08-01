package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

import org.es4j.eventstore.core.conversion.EventUpconverterPipelineHook;
import org.es4j.eventstore.core.conversion.Converter;
import java.util.Map;

/**
 *
 * @author Esfand
 */
public class UsingEventConverter {

    protected static Iterable<Assembly>                                     assemblies;
    protected static Map<Class/*Type*/, Converter/*Func*/<Object, Object>> converters;
    protected static EventUpconverterPipelineHook                           eventUpconverter;

    protected void establishContext() {
        assemblies       = getAllAssemblies();
        converters       = getConverters(assemblies);
        eventUpconverter = new EventUpconverterPipelineHook(converters);
    }


    private static Map<Class/*Type*/, Converter/*Func*/<Object, Object>> getConverters(Iterable<Assembly> toScan) {
        throw new RuntimeException(); /* for now
        var c = from a in toScan
                from t in a.GetTypes()
                let i = t.GetInterface(typeof(IUpconvertEvents<,>).FullName)
                where i != null
                let sourceType = i.getGenericArguments().First()
                let convertMethod = i.getMethods(BindingFlags.Public | BindingFlags.Instance).First()
                let instance = Activator.CreateInstance(t)
                select new KeyValuePair<Type, Func<Object, Object>>(
                sourceType,
                    e => convertMethod.Invoke(instance, new[] { e }));
			try {
				return c.ToDictionary(x => x.Key, x => x.Value);
			}
			catch (IllegalArgumentException ex) {
				throw new MultipleConvertersFoundException(ex.getMessage(), ex);
			} */
    }

    private static Iterable<Assembly> getAllAssemblies() {
        throw new RuntimeException(); /*
        return Assembly.getCallingAssembly()
                       .getReferencedAssemblies()
                       .select(Assembly.Load)
                       .concat(new[] { Assembly.getCallingAssembly() }); */
    }

}
























/*
	public class using_event_converter
	{
		protected static IEnumerable<Assembly> assemblies;
		protected static Dictionary<Type, Func<object, object>> converters;
		protected static EventUpconverterPipelineHook eventUpconverter;

		Establish context = () =>
		{
			assemblies = GetAllAssemblies();
			converters = GetConverters(assemblies);
			eventUpconverter = new EventUpconverterPipelineHook(converters);
		};

		private static Dictionary<Type, Func<object, object>> GetConverters(IEnumerable<Assembly> toScan)
		{
			var c = from a in toScan
					from t in a.GetTypes()
					let i = t.GetInterface(typeof(IUpconvertEvents<,>).FullName)
					where i != null
					let sourceType = i.GetGenericArguments().First()
					let convertMethod = i.GetMethods(BindingFlags.Public | BindingFlags.Instance).First()
					let instance = Activator.CreateInstance(t)
					select new KeyValuePair<Type, Func<object, object>>(
						sourceType,
						e => convertMethod.Invoke(instance, new[] { e }));
			try
			{
				return c.ToDictionary(x => x.Key, x => x.Value);
			}
			catch (ArgumentException ex)
			{
				throw new MultipleConvertersFoundException(ex.Message, ex);
			}
		}

		private static IEnumerable<Assembly> GetAllAssemblies()
		{
			return Assembly.GetCallingAssembly()
				.GetReferencedAssemblies()
				.Select(Assembly.Load)
				.Concat(new[] { Assembly.GetCallingAssembly() });
		}
	}

	public class ConvertingEventConverter : IUpconvertEvents<ConvertingEvent, ConvertingEvent2>
	{
		public ConvertingEvent2 Convert(ConvertingEvent sourceEvent)
		{
			return new ConvertingEvent2(sourceEvent.Id, "Temp");
		}
	}
	public class ExplicitConvertingEventConverter : IUpconvertEvents<ConvertingEvent2, ConvertingEvent3>
	{
		ConvertingEvent3 IUpconvertEvents<ConvertingEvent2, ConvertingEvent3>.Convert(ConvertingEvent2 sourceEvent)
		{
			return new ConvertingEvent3(sourceEvent.Id, "Temp", true);
		}
	}
	public class NonConvertingEvent
	{
	}
	public class ConvertingEvent
	{
		public Guid Id { get; set; }
		public ConvertingEvent(Guid id)
		{
			this.Id = id;
		}
	}
	public class ConvertingEvent2
	{
		public Guid Id { get; set; }
		public string Name { get; set; }

		public ConvertingEvent2(Guid id, string name)
		{
			this.Id = id;
			this.Name = name;
		}
	}
	public class ConvertingEvent3
	{
		public Guid Id { get; set; }
		public string Name { get; set; }
		public bool ImExplicit { get; set; }

		public ConvertingEvent3(Guid id, string name, bool imExplicit)
		{
			this.Id = id;
			this.Name = name;
			this.ImExplicit = imExplicit;
		}
}
*/
